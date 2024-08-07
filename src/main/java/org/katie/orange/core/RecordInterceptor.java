package org.katie.orange.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.katie.orange.core.data.methods.MethodCall;
import org.katie.orange.core.data.methods.MethodResult;
import org.katie.orange.core.data.objects.Node;

import java.lang.reflect.Method;
import java.util.Set;

public class RecordInterceptor {
    public static final Set<Class<?>> primitiveClasses = Set.of(Integer.class, Byte.class, Character.class, Boolean.class, Double.class, Float.class, Long.class, Short.class, int.class, byte.class, char.class, boolean.class, double.class, float.class, long.class, short.class);
    public static final Set<Class<?>> voidClasses = Set.of(Void.class, void.class);

    private final Listener listener;

    public RecordInterceptor(Listener listener) {
        this.listener = listener;
    }

    private boolean shouldSerialize(Object obj) {
        if (obj == null) {
            return true;
        }
        if (primitiveClasses.contains(obj.getClass())) {
            return true;
        }
        return voidClasses.contains(obj.getClass());
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments, @Origin Method orignalMethod, @This Object self) throws Exception {
        Node source = (Node) self.getClass().getField("objectNode").get(self);

        try {
            Object original = self.getClass().getField("original").get(self);
            Object returnValue = orignalMethod.invoke(original, allArguments);
            if (orignalMethod.getReturnType() == void.class) {
                return null;
            } else if (shouldSerialize(returnValue)) {
                Node dest = new Node(returnValue.getClass(), returnValue.toString());
                MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.RETURN);
                source.addEdge(edge);
                return returnValue;
            } else {
                Node dest = new Node(returnValue.getClass());
                MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.RETURN);
                source.addEdge(edge);
                return listener.createInternal(returnValue, dest);
            }
        } catch (Exception e) {
            Node dest = new Node(e.getClass());
            MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.THROW);
            source.addEdge(edge);
            if (dest.isTerminal()) {
                throw e;
            }
            throw e;
            //throw new MockFactory2().createInternal(e, dest);
        }
    }
}
