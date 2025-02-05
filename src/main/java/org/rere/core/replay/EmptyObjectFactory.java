package org.rere.core.replay;

public interface EmptyObjectFactory {
    <T> T getObject(Class<T> clazz);
}
