package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.InternalMethodCall;

import java.util.List;

public class PrimitiveWrapperNode {

    private final String className;


    public String getValue() {
        return value;
    }

    private final String value;
    public PrimitiveWrapperNode(String className, String value) {
        this.className = className;
        this.value = value;
    }

    public PrimitiveWrapperNode(Object o) {
        this(o.getClass().getName(), o.toString());
    }
    public <T> PrimitiveWrapperNode(Class<?> clazz, T value) {
        this.className = clazz.getName();
        this.value = value.toString();
    }
    public String getDisplayName() {
        return className + ":" + value;
    }
}
