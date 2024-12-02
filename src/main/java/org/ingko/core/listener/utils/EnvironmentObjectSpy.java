package org.ingko.core.listener.utils;

import org.ingko.core.data.objects.EnvironmentNode;

public interface EnvironmentObjectSpy extends ObjectSpy {
    EnvironmentNode getParrotNodePointer();
    void setParrotNodePointer(EnvironmentNode node);
    String FIELD = "parrotNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
