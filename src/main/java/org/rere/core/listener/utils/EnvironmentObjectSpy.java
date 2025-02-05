package org.rere.core.listener.utils;

import org.rere.core.data.objects.EnvironmentNode;

public interface EnvironmentObjectSpy extends ObjectSpy {
    EnvironmentNode getReReNodePointer();
    void setReReNodePointer(EnvironmentNode node);
    String FIELD = "reReNodePointer";
    Class<?> TYPE = EnvironmentNode.class;
}
