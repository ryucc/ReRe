package org.ingko.core.data.methods;

import org.ingko.core.data.objects.EnvironmentNode;

import java.io.Serializable;
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

    private final Class<?> returnType;

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
}
