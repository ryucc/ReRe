package org.katie.orange.core.data.methods;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Signature {
    private final String returnType;
    private final String methodName;
    private final List<String> paramTypes;
    public Signature(Method method) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.paramTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
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
}
