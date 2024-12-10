package org.ingko.core.listener.serialization;

import org.ingko.core.data.objects.EnvironmentNode;

public interface ParrotSerialization {
    boolean accept(Class<?> clazz);
    EnvironmentNode getSerializedNode(Object o, Class<?> clazz);
}
