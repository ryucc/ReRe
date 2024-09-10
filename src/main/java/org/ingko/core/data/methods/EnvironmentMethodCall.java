package org.ingko.core.data.methods;

import org.ingko.core.data.objects.EnvironmentNode;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.UUID;

public class EnvironmentMethodCall implements Serializable {

    public Signature getSignature() {
        return signature;
    }

    private final Signature signature;

    // Dynamic
    private final UUID uuid;
    private final EnvironmentNode source;

    public EnvironmentNode getDest() {
        return dest;
    }

    private final EnvironmentNode dest;
    private final MethodResult result;

    public EnvironmentMethodCall(Method method, EnvironmentNode source, EnvironmentNode dest, MethodResult result) {
        this.dest = dest;
        this.source = source;
        this.result = result;
        this.uuid = UUID.randomUUID();
        this.signature = new Signature(method);
    }

    public String getName() {
        return signature.getMethodName();
    }
    public MethodResult getResult() {
        return result;
    }

    public boolean isVoid() {
        return signature.getReturnType().equals("java.lang.Void") || signature.getReturnType().equals("void");
    }
}
