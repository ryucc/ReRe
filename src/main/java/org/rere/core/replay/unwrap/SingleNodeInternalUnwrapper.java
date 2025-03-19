/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;

import java.util.ArrayList;
import java.util.List;

public class SingleNodeInternalUnwrapper implements ReplayInternalUnwrapper {
    private final List<ReplayInternalUnwrapper> unwrapperList;

    public SingleNodeInternalUnwrapper() {
        this.unwrapperList = new ArrayList<>();
    }

    public void registerChild(ReplayInternalUnwrapper unwrapper) {
        this.unwrapperList.add(unwrapper);
    }

    @Override
    public Object unwrap(EnvironmentNode node) {
        for(ReplayInternalUnwrapper unwrapper: unwrapperList) {
            if(unwrapper.accept(node)) {
                return unwrapper.unwrap(node);
            }
        }
        throw new RuntimeException("No suitable unwrappers found for node: " + node.getRuntimeClass());
    }

    @Override
    public boolean accept(EnvironmentNode node) {
        return true;
    }
}
