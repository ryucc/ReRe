package org.ingko.core.data.methods;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Signature implements Serializable {
    private final String returnType;
    private final Type returnClass;
    private final String methodName;
    private final List<String> paramTypes;
    private final List<Class<?>> paramClasses;
    public Signature(Method method) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.returnClass = method.getGenericReturnType();
        this.paramTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        this.paramClasses = Arrays.asList(method.getParameterTypes());
    }

    public Type getReturnClass() {
        return returnClass;
    }

    public List<Class<?>> getParamClasses() {
        return paramClasses;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += methodName.hashCode();
        for (String param : paramTypes) {
            hash += param.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Signature other)) {
            return false;
        }
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
