package org.katie.orange.core.data.methods;

import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.objects.PrimitiveWrapperNode;
import org.katie.orange.core.data.objects.ThrowableNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class PrimitiveMethodCall {

    // signature
    private final String returnType;
    private final UUID uuid;
    // Dynamic
    private final InternalNode source;
    private final Optional<PrimitiveWrapperNode> dest;
    private final Optional<ThrowableNode> throwableDest;

    public MethodResult getResult() {
        return result;
    }

    private final MethodResult result;

    public Signature getSignature() {
        return signature;
    }

    private final Signature signature;

    public PrimitiveMethodCall(Method method, InternalNode source, PrimitiveWrapperNode dest) {
        this.signature = new Signature(method);
        this.returnType = method.getReturnType().getName();
        this.source = source;
        this.dest = Optional.of(dest);
        this.throwableDest = Optional.empty();
        this.result = MethodResult.RETURN;
        this.uuid = UUID.randomUUID();
    }
    public PrimitiveMethodCall(Method method, InternalNode source, ThrowableNode dest) {
        this.signature = new Signature(method);
        this.returnType = method.getReturnType().getName();
        this.source = source;
        this.dest = Optional.empty();
        this.throwableDest = Optional.of(dest);
        this.result = MethodResult.RETURN;
        this.uuid = UUID.randomUUID();
    }

    public PrimitiveWrapperNode getDest() {
        return dest.get();
    }
}
