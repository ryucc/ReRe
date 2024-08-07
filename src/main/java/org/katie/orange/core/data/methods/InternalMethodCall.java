package org.katie.orange.core.data.methods;

import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.objects.ThrowableNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class InternalMethodCall {

    // signature
    private final String methodName;
    private final String returnType;

    private final Signature signature;
    private final List<String> parameterTypes;
    private final UUID uuid;
    // Dynamic
    private final InternalNode source;
    private final Optional<InternalNode> dest;
    private final Optional<ThrowableNode> throwableDest;

    public MethodResult getResult() {
        return result;
    }

    private final MethodResult result;

    public InternalMethodCall(Method method, InternalNode source, InternalNode dest) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.parameterTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        this.source = source;
        this.dest = Optional.of(dest);
        this.throwableDest = Optional.empty();
        this.result = MethodResult.RETURN;
        this.uuid = UUID.randomUUID();
        this.signature = new Signature(method);
    }
    public InternalMethodCall(Method method, InternalNode source, ThrowableNode dest) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.parameterTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.toList());
        this.source = source;
        this.dest = Optional.empty();
        this.throwableDest = Optional.of(dest);
        this.result = MethodResult.RETURN;
        this.uuid = UUID.randomUUID();
        this.signature = new Signature(method);
    }
    public Signature getSignature() {
        return signature;
    }

    public String getName() {
        String[] a = methodName.split("\\.");
        return a[a.length - 1];
    }

    public InternalNode getDest() {
        if (dest.isPresent()) {
            return dest.get();
        } else {
            // print warning
            return null;
        }
    }

    public String getReturnType() {
        return returnType;
    }
}
