package org.katie.orange.core.data.methods;

import org.katie.orange.core.data.objects.InternalNode;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;

public class VoidMethodCall {
    private final String methodName;
    private final String[] parameterTypes;
    private final MethodResult result;

    /*
    private final Instant startTime;
    private final Instant endTime;
     */

    public VoidMethodCall(Method method, InternalNode source, MethodResult result) {
        this.methodName = method.getName();
        this.parameterTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray(String[]::new);
        this.result = result;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public MethodResult getResult() {
        return result;
    }

    public String getMethodName() {
        return methodName;
    }
}
