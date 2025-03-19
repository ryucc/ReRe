/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

import org.rere.api.ReReplayData;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.EnvironmentNodeManager;

import java.util.ArrayList;

public class EnvironmentObjectWrapper implements ReReRootObjectWrapper{

    private final ReReplayData reReplayData;
    private final TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> topoOrderWrapper;

    public EnvironmentObjectWrapper(EnvironmentNodeManager nodeManager) {
        this.topoOrderWrapper = new TopoOrderObjectWrapper<>(nodeManager);
        reReplayData = new ReReplayData(new ArrayList<>());
    }

    public <T> ReReWrapResult<T, EnvironmentNode> wrapObject(Object original, Class<T> targetClass) {
        return topoOrderWrapper.createRoot(original, targetClass);
    }

    public <T> T wrapRootObject(Object original, Class<T> targetClass) {
        ReReWrapResult<T, EnvironmentNode> result = topoOrderWrapper.createRoot(original, targetClass);
        reReplayData.roots().add(result.node());
        return result.wrapped();
    }

    @Override
    public ReReplayData getReplayData() {
        return this.reReplayData;
    }
}
