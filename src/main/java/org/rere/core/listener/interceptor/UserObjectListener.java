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
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.listener.wrap.ReReWrapResult;
import org.rere.core.listener.wrap.TopoOrderObjectWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class UserObjectListener implements ReReMethodInterceptor<UserNode> {

    private final TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> environmentObjectWrapper;
    private final TopoOrderObjectWrapper<UserNode, UserNodeManager> userObjectWrapper;

    public UserObjectListener(TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
        this.userObjectWrapper = new TopoOrderObjectWrapper<>(new UserNodeManager(this));
    }

    public ReReWrapResult<?, UserNode> createRoot(Object original,
                                                  Class<?> type,
                                                  EnvironmentMethodCall scope,
                                                  LocalSymbol symbol) {
        ReReWrapResult<?, UserNode> wrapped = userObjectWrapper.createRoot(original, type);

        // DFS
        Queue<UserNode> nodeQueue = new ArrayDeque<>();
        Set<UserNode> explored = new HashSet<>();
        nodeQueue.add(wrapped.node());
        wrapped.node().setSymbol(symbol);

        while (!nodeQueue.isEmpty()) {
            UserNode cur = nodeQueue.poll();
            cur.setScope(scope);
            explored.add(cur);
            if (cur.getDeclaredClass().isArray()) {
                for (int i = 0; i < cur.getDirectChildren().size(); i++) {
                    UserNode child = cur.getDirectChildren().get(i);
                    if (!explored.contains(child)) {
                        LocalSymbol childSymbol = cur.getSymbol().copy();
                        childSymbol.appendPath(new ArrayMember(i));
                        child.setSymbol(childSymbol);
                        nodeQueue.add(child);
                    }
                }
            } else if (ClassUtils.isRecord(cur.getDeclaredClass())) {
                Field[] recordComponents = cur.getDeclaredClass().getDeclaredFields();
                for (int i = 0; i < cur.getDirectChildren().size(); i++) {
                    UserNode child = cur.getDirectChildren().get(i);
                    if (!explored.contains(child)) {
                        LocalSymbol childSymbol = cur.getSymbol().copy();
                        Field component = recordComponents[i];
                        childSymbol.appendPath(new RecordMember(component.getName()));
                        child.setSymbol(childSymbol);
                        nodeQueue.add(child);
                    }
                }
            }
        }
        return new ReReWrapResult<>(wrapped.wrapped(), wrapped.node());
    }

    public Object interceptInterface(Object original,
                                     Method orignalMethod,
                                     UserNode userNode,
                                     Object[] allArguments) throws Throwable {
        List<EnvironmentNode> environmentNodes = new ArrayList<>();
        List<Object> wrappedArguments = new ArrayList<>();
        List<LocalSymbol> parameterSourceList = new ArrayList<>();
        for (Object arg : allArguments) {
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
                ReReWrapResult<?, EnvironmentNode> listenResult = environmentObjectWrapper.createRoot(arg,
                        arg.getClass());

                int curIndex = environmentNodes.size();
                LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, curIndex);
                parameterSourceList.add(symbol);

                environmentNodes.add(listenResult.node());
                wrappedArguments.add(listenResult.wrapped());
            }
        }
        // register method call to scope

        EnvironmentMethodCall scopeMethod = userNode.getScope();

        String methodName = orignalMethod.getName();
        LocalSymbol operand = userNode.getSymbol();
        UserMethodCall userMethodCall = new UserMethodCall(operand,
                methodName,
                environmentNodes,
                parameterSourceList);
        scopeMethod.addUserMethodCall(userMethodCall);

        // We can set the return values after registering...
        // The return values only need a pointer to the scope and its index.

        int currentReturnIndex = scopeMethod.getLastReturnIndex();

        try {
            orignalMethod.setAccessible(true);
            Object ret = orignalMethod.invoke(original, wrappedArguments.toArray());
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
            ReReWrapResult<?, UserNode> result = createRoot(ret, orignalMethod.getReturnType(), scopeMethod, symbol);
            return result.wrapped();
        } catch (InvocationTargetException e) {
            Throwable real = e.getTargetException();
            LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.THROW, currentReturnIndex);
            ReReWrapResult<?, UserNode> result = createRoot(real, real.getClass(), scopeMethod, symbol);
            result.node().setSymbol(symbol);
            throw (Throwable) result.wrapped();
        } catch (IllegalAccessException e) {
            // ReRe does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }
    }
}
