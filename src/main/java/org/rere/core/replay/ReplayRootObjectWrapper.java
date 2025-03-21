/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.api.ReReSettings;
import org.rere.api.ReReplayData;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.spies.ObjectSpy;
import org.rere.core.replay.unwrap.LeafNodeInternalUnwrapper;
import org.rere.core.replay.unwrap.PrimitiveInternalUnwrapper;
import org.rere.core.replay.unwrap.ReplayObjectWrapper;
import org.rere.core.replay.unwrap.SerializedInternalUnwrapper;
import org.rere.core.replay.unwrap.SingleNodeInternalUnwrapper;
import org.rere.core.wrap.ReReRootObjectWrapper;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;

public class ReplayRootObjectWrapper implements ReReRootObjectWrapper {
    private final ReReplayData reReplayData;
    private final ReplayObjectWrapper replayObjectWrapper;

    public ReplayRootObjectWrapper(ReReSettings reReSettings) {
        if (!reReSettings.getReReData().isPresent()) {
            throw new RuntimeException("No Replay data for Replay mode!");
        }
        this.reReplayData = reReSettings.getReReData().get().getReReplayData();

        ReplayObjectListener replayObjectListener = new ReplayObjectListener();
        MockitoSingleNodeWrapper<InOrderReplayNode> wrapper = new MockitoSingleNodeWrapper<>(replayObjectListener,
                ObjectSpy.class);
        SingleNodeInternalUnwrapper singleNodeUnwrapper = new SingleNodeInternalUnwrapper();
        singleNodeUnwrapper.registerChild(new PrimitiveInternalUnwrapper());
        singleNodeUnwrapper.registerChild(new SerializedInternalUnwrapper());
        singleNodeUnwrapper.registerChild(new LeafNodeInternalUnwrapper(wrapper));
        replayObjectWrapper = new ReplayObjectWrapper(singleNodeUnwrapper);
        replayObjectListener.setReplayObjectWrapper(replayObjectWrapper);
    }

    @Override
    public <T> T wrapRootObject(Object original, Class<T> targetClass) {
        EnvironmentNode node = reReplayData.roots().get(0);
        return targetClass.cast(replayObjectWrapper.unwrap(node));
    }

    @Override
    public ReReplayData getReplayData() {
        return reReplayData;
    }
}
