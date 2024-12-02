package org.ingko.core.listener.utils;

public interface ObjectSpy {
    void setParrotOriginObject(Object original);
    Object getParrotOriginObject();
    String FIELD = "parrotOriginObject";
    Class<?> TYPE = Object.class;
}
