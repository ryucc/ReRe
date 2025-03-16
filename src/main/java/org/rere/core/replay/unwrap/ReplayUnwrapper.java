/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;

public interface ReplayUnwrapper {
    Object unwrap(EnvironmentNode node);
    boolean accept(EnvironmentNode node);
}
