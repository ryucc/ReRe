/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.methods;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class UserMethodCall implements Serializable {

    public LocalSymbol getSource() {
        return source;
    }

    private LocalSymbol source;

    public List<LocalSymbol> getParameters() {
        return parameters;
    }

    private List<LocalSymbol> parameters;

    public List<EnvironmentNode> getLocalParameters() {
        return localParameters;
    }

    private final List<EnvironmentNode> localParameters;

    public String getMethodName() {
        return methodName;
    }

    private final String methodName;

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    private Class<?> returnType;

    public UserMethodCall(LocalSymbol source,
                          String methodName,
                          List<EnvironmentNode> local,
                          List<LocalSymbol> parameters,
                          Class<?> returnType) {
        this.source = source;
        this.localParameters = local;
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = returnType;
    }
    public UserMethodCall(LocalSymbol source,
                           String methodName,
                           List<EnvironmentNode> local,
                           List<LocalSymbol> parameters) {
        this.source = source;
        this.localParameters = local;
        this.methodName = methodName;
        this.parameters = parameters;
    }
}
