/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.interceptor;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.spies.EnvironmentObjectSpy;
import org.rere.core.listener.spies.UserObjectSpy;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.mockito.UserObjectWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UserObjectListener implements ReReMethodInterceptor<UserNode> {

    private final EnvironmentObjectWrapper environmentObjectWrapper;
    private UserObjectWrapper userObjectWrapper;

    public UserObjectListener(EnvironmentObjectWrapper environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
    }

    public void setUserObjectWrapper(UserObjectWrapper userObjectWrapper) {
        this.userObjectWrapper = userObjectWrapper;
    }

    public Method findMethod(Class<?> clazz, String methodName, Object[] allArguments) throws NoSuchMethodException {
        List<Method> candidates = new ArrayList<>();
        for(Method method: clazz.getMethods()) {
            if(!method.getName().equals(methodName)) continue;
            if(method.getParameterTypes().length != allArguments.length) continue;
            boolean match = true;
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                if(allArguments[i] == null) continue;
                Class<?> actualClass = allArguments[i].getClass();
                if(!method.getParameterTypes()[i].isAssignableFrom(actualClass)) {
                    match = false;
                    break;
                }
            }
            if(match) {
                candidates.add(method);
            }
        }
        if(candidates.isEmpty()) {
            throw new NoSuchMethodException();
        }

        Method bestMethod = candidates.get(0);
        for(Method next: candidates) {
            for(int i = 0; i < bestMethod.getParameterCount(); i++) {
                if(!next.getParameterTypes()[i].isAssignableFrom(bestMethod.getParameterTypes()[i])) {
                    bestMethod = next;
                }
            }
        }
        return bestMethod;
    }
    public Object interceptInterface(Object original,
                                     Method implementedMethod,
                                     UserNode userNode,
                                     Object[] allArguments) throws Throwable {
        Method originalMethod;
        try {
            originalMethod = findMethod(userNode.getRuntimeClass(), implementedMethod.getName(), allArguments);
        } catch (NoSuchMethodException e) {
            originalMethod = implementedMethod;
        }
        List<EnvironmentNode> environmentNodes = new ArrayList<>();
        List<Object> wrappedArguments = new ArrayList<>();
        List<LocalSymbol> parameterSourceList = new ArrayList<>();
        EnvironmentMethodCall scopeMethod = userNode.getScope();
        for (int i = 0; i < allArguments.length; i++) {
            Object arg = allArguments[i];
            Class<?> representingClass = originalMethod.getParameterTypes()[i];
            if (arg instanceof UserObjectSpy) {
                UserNode node = ((UserObjectSpy) arg).getReReUserNode();
                parameterSourceList.add(node.getSymbol());
                Object origin = ((UserObjectSpy) arg).getReReOriginObject();
                wrappedArguments.add(origin);
            } else if (arg instanceof EnvironmentObjectSpy) {
                // should never happen?
                // nope, this will happen
                // Can ignore only if this is a stateless non user-environment node.
                // Let's assume environments are stateless first... handle this later.
                System.err.println("User method call received environment object as parameter.");
            } else {
                boolean foundMatch = false;
                for(int j =0; j < scopeMethod.getFailedUserObjects().size(); j++) {
                    if (scopeMethod.getFailedUserObjects().get(j) == arg) {
                        wrappedArguments.add(arg);
                        UserNode node = scopeMethod.getFailedUserNodes().get(j);
                        parameterSourceList.add(node.getSymbol());
                        foundMatch = true;
                        break;
                    }
                }
                if(foundMatch) {
                    continue;
                }
                // TODO
                ReReWrapResult<?, EnvironmentNode> listenResult =
                        environmentObjectWrapper.wrapObject(arg, representingClass);

                int curIndex = environmentNodes.size();
                LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, curIndex);
                parameterSourceList.add(symbol);

                environmentNodes.add(listenResult.node());
                wrappedArguments.add(listenResult.wrapped());
            }
        }
        // register method call to scope
        String methodName = implementedMethod.getName();
        LocalSymbol operand = userNode.getSymbol();
        UserMethodCall userMethodCall = new UserMethodCall(operand, methodName, environmentNodes, parameterSourceList);
        scopeMethod.addUserMethodCall(userMethodCall);

        // We can set the return values after registering...
        // The return values only need a pointer to the scope and its index.

        int currentReturnIndex = scopeMethod.getLastReturnIndex();

        try {
            implementedMethod.setAccessible(true);
            Object ret = implementedMethod.invoke(original, wrappedArguments.toArray());
            if (ret == null) {
                // BUG did not set return node
                // TODO: check everywhere to set return node
                userMethodCall.setReturnNode(new UserNode(originalMethod.getReturnType(), originalMethod.getReturnType()));
                return null;
            } else {
                userMethodCall.setReturnType(ret.getClass());
            }

            if (ret instanceof EnvironmentObjectSpy) {
                // Environment object: untrack. Even if its member is user object, the method calls will be traced.
                // Stateful: need to make it into a environment - user object
                userMethodCall.setReturnNode(new UserNode(originalMethod.getReturnType(), originalMethod.getReturnType()));
                return ((EnvironmentObjectSpy) ret).getReReOriginObject();
            }
            if (ret instanceof UserObjectSpy) {
                // User Object: do nothing is okay, but rewrap is probably better. (Can register new localSymbol)
                // Rewrapping makings synthesized code look closer to original code.
                ret = ((UserObjectSpy) ret).getReReOriginObject();
            }
            LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, currentReturnIndex);
            ReReWrapResult<?, UserNode> result = userObjectWrapper.createRoot(ret, originalMethod.getReturnType(), scopeMethod, symbol);
            userMethodCall.setReturnNode(result.node());
            if(result.node().isFailedNode()) {
                scopeMethod.getFailedUserObjects().add(ret);
                scopeMethod.getFailedUserNodes().add(result.node());
            }
            return result.wrapped();
        } catch (InvocationTargetException e) {
            Throwable real = e.getTargetException();
            LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.THROW, currentReturnIndex);
            ReReWrapResult<?, UserNode> result = userObjectWrapper.createRoot(real, real.getClass(), scopeMethod, symbol);
            result.node().setSymbol(symbol);
            userMethodCall.setReturnNode(result.node());
            throw (Throwable) result.wrapped();
        } catch (IllegalAccessException e) {
            // ReRe does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }
    }
}
