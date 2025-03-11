/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.spies;

import org.rere.core.data.objects.EnvironmentNode;

public interface EnvironmentObjectSpy extends ObjectSpy {
    EnvironmentNode getReReNodePointer();
    void setReReNodePointer(EnvironmentNode node);
    String FIELD = "reReNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
