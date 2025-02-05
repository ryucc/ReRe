/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.wrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.EnvironmentNodeManager;

public class EnvironmentObjectWrapper extends TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> {
    public EnvironmentObjectWrapper(EnvironmentNodeManager nodeManager) {
        super(nodeManager);
    }
}
