/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.methods;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.objects.UserNode;

import java.io.Serializable;
import java.util.List;

public class UserMethodCall implements Serializable {

    private final List<EnvironmentNode> localParameters;
    private final String methodName;
    private final LocalSymbol source;
    private final List<LocalSymbol> parameters;
    private Class<?> returnType;
    private UserNode returnNode;

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

    public UserNode getReturnNode() {
        return returnNode;
    }

    public void setReturnNode(UserNode returnNode) {
        this.returnNode = returnNode;
    }

    public LocalSymbol getSource() {
        return source;
    }

    public List<LocalSymbol> getParameters() {
        return parameters;
    }

    public List<EnvironmentNode> getLocalParameters() {
        return localParameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
