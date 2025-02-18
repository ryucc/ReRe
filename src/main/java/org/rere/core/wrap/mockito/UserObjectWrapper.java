/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap.mockito;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.ArrayMember;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.objects.RecordMember;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.TopoOrderObjectWrapper;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class UserObjectWrapper {


    private final TopoOrderObjectWrapper<UserNode, UserNodeManager> topoOrderWrapper;

    public UserObjectWrapper(TopoOrderObjectWrapper<UserNode, UserNodeManager> topoOrderWrapper) {
        this.topoOrderWrapper = topoOrderWrapper;
    }

    public ReReWrapResult<?, UserNode> createRoot(Object original,
                                                  Class<?> type,
                                                  EnvironmentMethodCall scope,
                                                  LocalSymbol symbol) {
        ReReWrapResult<?, UserNode> wrapped = topoOrderWrapper.createRoot(original, type);

        // DFS
        Queue<UserNode> nodeQueue = new ArrayDeque<>();
        Set<UserNode> explored = new HashSet<>();
        nodeQueue.add(wrapped.node());
        wrapped.node().setSymbol(symbol);

        while (!nodeQueue.isEmpty()) {
            UserNode cur = nodeQueue.poll();
            cur.setScope(scope);
            explored.add(cur);
            if (cur.getRuntimeClass().isArray()) {
                for (int i = 0; i < cur.getDirectChildren().size(); i++) {
                    UserNode child = cur.getDirectChildren().get(i);
                    if (!explored.contains(child)) {
                        LocalSymbol childSymbol = cur.getSymbol().copy();
                        childSymbol.appendPath(new ArrayMember(i));
                        child.setSymbol(childSymbol);
                        nodeQueue.add(child);
                    }
                }
            } else if (ClassUtils.isRecord(cur.getRuntimeClass())) {
                Field[] recordComponents = cur.getRuntimeClass().getDeclaredFields();
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
}
