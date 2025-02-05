package org.rere.core.listener.utils;

public interface ObjectSpy {
    void setReReOriginObject(Object original);
    Object getReReOriginObject();
    String FIELD = "reReOriginObject";
    Class<?> TYPE = Object.class;
}
