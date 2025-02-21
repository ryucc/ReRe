/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.interceptor;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.ArrayMember;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.objects.RecordMember;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReEnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.TopoOrderObjectWrapper;
import org.rere.core.wrap.mockito.UserObjectWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import java.net.http.HttpHeaders;
public class UserObjectListener implements ReReMethodInterceptor<UserNode> {

    private final EnvironmentObjectWrapper environmentObjectWrapper;
    private UserObjectWrapper userObjectWrapper;

    public UserObjectListener(EnvironmentObjectWrapper environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
    }

    public void setUserObjectWrapper(UserObjectWrapper userObjectWrapper) {
        this.userObjectWrapper = userObjectWrapper;
    }
    public Object interceptInterface(Object original,
                                     Method originalMethod,
                                     UserNode userNode,
                                     Object[] allArguments) throws Throwable {
        List<EnvironmentNode> environmentNodes = new ArrayList<>();
        List<Object> wrappedArguments = new ArrayList<>();
        List<LocalSymbol> parameterSourceList = new ArrayList<>();
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
                // TODO
                ReReWrapResult<?, EnvironmentNode> listenResult =
                        environmentObjectWrapper.createRoot(arg, representingClass);

                int curIndex = environmentNodes.size();
                LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, curIndex);
                parameterSourceList.add(symbol);

                environmentNodes.add(listenResult.node());
                wrappedArguments.add(listenResult.wrapped());
            }
        }
        // register method call to scope

        EnvironmentMethodCall scopeMethod = userNode.getScope();

        String methodName = originalMethod.getName();
        LocalSymbol operand = userNode.getSymbol();
        UserMethodCall userMethodCall = new UserMethodCall(operand, methodName, environmentNodes, parameterSourceList);
        scopeMethod.addUserMethodCall(userMethodCall);

        // We can set the return values after registering...
        // The return values only need a pointer to the scope and its index.

        int currentReturnIndex = scopeMethod.getLastReturnIndex();

        try {
            originalMethod.setAccessible(true);
            Object ret = originalMethod.invoke(original, wrappedArguments.toArray());
            if (ret == null) {
                return null;
            } else {
                userMethodCall.setReturnType(ret.getClass());
            }

            if (ret instanceof EnvironmentObjectSpy) {
                // Environment object: untrack. Even if its member is user object, the method calls will be traced.
                // Stateful: need to make it into a environment - user object
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
