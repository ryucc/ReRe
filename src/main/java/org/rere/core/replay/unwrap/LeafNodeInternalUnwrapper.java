/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.replay.InOrderReplayNode;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;

public class LeafNodeInternalUnwrapper implements ReplayInternalUnwrapper {
    public LeafNodeInternalUnwrapper(MockitoSingleNodeWrapper<InOrderReplayNode> singleNodeWrapper) {
        this.singleNodeWrapper = singleNodeWrapper;
    }

    private final MockitoSingleNodeWrapper<InOrderReplayNode> singleNodeWrapper;


    @Override
    public Object unwrap(EnvironmentNode node) {

        InOrderReplayNode replayNode = new InOrderReplayNode(node.getRepresentingClass(),
                node.getRepresentingClass(),
                node.getMethodCalls());
        return singleNodeWrapper.initiateSpied(null, replayNode);
    }

    @Override
    public boolean accept(EnvironmentNode node) {
        return node.getDirectChildren().isEmpty();
    }
}
