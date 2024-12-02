package org.ingko.core.replay.spies;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.utils.ObjectSpy;

public interface InOrderReplaySpy extends ObjectSpy {
    EnvironmentNode getParrotNodePointer();
    void setParrotNodePointer(EnvironmentNode node);

    String FIELD = "parrotNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
