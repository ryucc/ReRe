/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.methods;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Signature implements Serializable {
    private final String returnType;
    private final Type returnClass;
    private final String methodName;
    private final List<Type> paramTypes;
    private List<Class<?>> paramClasses;

    public Signature(Method method) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.returnClass = method.getGenericReturnType();
        this.paramTypes = Arrays.asList(method.getGenericParameterTypes());
    }

    public Type getReturnClass() {
        return returnClass;
    }

    public List<Class<?>> getParamClasses() {
        return paramClasses;
    }

    public void setParamClasses(List<Class<?>> paramClasses) {
        this.paramClasses = paramClasses;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += methodName.hashCode();
        for (Type param : paramTypes) {
            hash += param.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Signature)) {
            return false;
        }
        Signature other = (Signature) obj;
        if (!other.getMethodName().equals(this.methodName)) {
            return false;
        }
        if (other.getParamTypes().size() != this.paramTypes.size()) {
            return false;
        }
        for (int i = 0; i < this.paramTypes.size(); i++) {
            if (!other.getParamTypes().get(i).equals(this.paramTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s %s()", returnType, methodName);
    }

}
