/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.core.data.objects.ReReObjectNode;

import java.util.List;

public class ReplayNode implements ReReObjectNode<ReplayNode> {
    @Override
    public Class<?> getRepresentingClass() {
        return null;
    }

    @Override
    public Class<?> getRuntimeClass() {
        return null;
    }

    @Override
    public void setFailedNode(boolean flag) {

    }

    @Override
    public void setComments(String comments) {

    }

    @Override
    public void addChild(ReplayNode child) {

    }

    @Override
    public List<? extends ReReObjectNode<ReplayNode>> getDirectChildren() {
        return null;
    }
}
