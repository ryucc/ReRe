package org.rere.core.replay.spies;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.utils.ObjectSpy;

public interface InOrderReplaySpy extends ObjectSpy {
    EnvironmentNode getReReNodePointer();
    void setReReNodePointer(EnvironmentNode node);

    String FIELD = "reReNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
