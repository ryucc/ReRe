/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.data.objects.EnvironmentNode;

import java.util.List;

public class ReReplayData {
    private final List<EnvironmentNode> roots;

    public ReReplayData(List<EnvironmentNode> roots) {
        this.roots = roots;
    }

    public List<EnvironmentNode> roots() {
        return roots;
    }

}
