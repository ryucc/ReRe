/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.spies;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.spies.ObjectSpy;

public interface InOrderReplaySpy extends ObjectSpy {
    EnvironmentNode getReReNodePointer();
    void setReReNodePointer(EnvironmentNode node);



    String FIELD = "reReNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
