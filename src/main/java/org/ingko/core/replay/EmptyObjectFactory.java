package org.ingko.core.replay;

public interface EmptyObjectFactory {
    <T> T getObject(Class<T> clazz);
}
