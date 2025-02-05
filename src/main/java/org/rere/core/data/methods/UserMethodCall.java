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

    public Type getReturnType() {
        return returnType;
    }

    private final Type returnType;

    public UserMethodCall(LocalSymbol source,
                          String methodName,
                          List<EnvironmentNode> local,
                          List<LocalSymbol> parameters,
                          Type returnType) {
        this.source = source;
        this.localParameters = local;
        this.methodName = methodName;
        this.parameters = parameters;
        this.returnType = returnType;
    }
}
