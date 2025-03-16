/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.serde.PrimitiveSerde;

public class SerializedUnwrapper implements ReplayUnwrapper {
    @Override
    public Object unwrap(EnvironmentNode node) {
        return new PrimitiveSerde().deserialize(node.getValue());
    }

    @Override
    public boolean accept(EnvironmentNode node) {
        return node.isSerialized();
    }
}
