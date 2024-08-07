package org.katie.orange.core.data.methods;

import org.katie.orange.core.data.objects.Node;

import java.lang.reflect.Method;
import java.util.UUID;

public class MethodCall {

    private final Signature signature;

    // Dynamic
    private final UUID uuid;
    private final Node source;
    private final Node dest;
    private final MethodResult result;

    public MethodCall(Method method, Node source, Node dest, MethodResult result) {
        this.dest = dest;
        this.source = source;
        this.result = result;
        this.uuid = UUID.randomUUID();
        this.signature = new Signature(method);
    }

    public MethodResult getResult() {
        return result;
    }

    public boolean isVoid() {
        return signature.getReturnType().equals("java.lang.Void") || signature.getReturnType().equals("void");
    }
}
