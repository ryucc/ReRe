/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.UserNodeManager;

public class EnvironmentObjectWrapper {

    private final TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> topoOrderWrapper;
    public EnvironmentObjectWrapper(EnvironmentNodeManager nodeManager) {
        this.topoOrderWrapper = new TopoOrderObjectWrapper<>(nodeManager);
    }
    public <T> ReReWrapResult<T, EnvironmentNode> createRoot(Object original, Class<T> targetClass) {
        return topoOrderWrapper.createRoot(original, targetClass);
    }


}
