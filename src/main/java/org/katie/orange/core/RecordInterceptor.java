package org.katie.orange.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.katie.orange.core.data.methods.PrimitiveMethodCall;
import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.objects.PrimitiveWrapperNode;
import org.katie.orange.core.data.objects.ThrowableNode;

import java.lang.reflect.Method;
import java.util.Set;

public class RecordInterceptor {

    public static final Set<Class<?>> primitiveClasses = Set.of(Integer.class,
            Byte.class, Character.class, Boolean.class, Double.class, Float.class, Long.class, Short.class,
            int.class, byte.class, char.class, boolean.class, double.class, float.class, long.class, short.class);
    public static final Set<Class<?>> voidClasses = Set.of(Void.class, void.class);


    public RecordInterceptor() {
    }

    public InternalNode createInternalNode(Object o) {
        return new InternalNode(o.getClass());
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments, @Origin Method orignalMethod, @This Object self) throws Exception {
        if (primitiveClasses.contains(orignalMethod.getReturnType())) {
            return interceptPrimitive(allArguments, orignalMethod, self);
        } else if (voidClasses.contains(orignalMethod.getReturnType())) {
            System.out.println("VOID METHOD!!!");
            return null;
        } else {
            System.out.println(orignalMethod.getReturnType());
            return interceptInternal(allArguments, orignalMethod, self);
        }
    }

    private Object interceptPrimitive(@AllArguments Object[] allArguments, @Origin Method orignalMethod, @This Object self) throws Exception {

        InternalNode source = (InternalNode) self.getClass().getField("objectNode").get(self);

        try {
            Object original = self.getClass().getField("original").get(self);
            Object returnValue = orignalMethod.invoke(original, allArguments);
            PrimitiveWrapperNode primitiveWrapperNode = new PrimitiveWrapperNode(returnValue);
            PrimitiveMethodCall primitiveMethodCall = new PrimitiveMethodCall(orignalMethod, source, primitiveWrapperNode);
            source.addPrimitiveEdge(primitiveMethodCall);
            return returnValue;
        } catch (Exception e) {
            ThrowableNode dest = new ThrowableNode(e);
            PrimitiveMethodCall edge = new PrimitiveMethodCall(orignalMethod, source, dest);
            source.addPrimitiveEdge(edge);
            throw e;
        }
    }

    private Object interceptInternal(@AllArguments Object[] allArguments, @Origin Method orignalMethod, @This Object self) throws Exception {

        InternalNode source = (InternalNode) self.getClass().getField("objectNode").get(self);

        try {
            Object original = self.getClass().getField("original").get(self);
            Object returnValue = orignalMethod.invoke(original, allArguments);
            InternalNode dest = createInternalNode(returnValue);
            InternalMethodCall edge = new InternalMethodCall(orignalMethod, source, dest);
            source.addEdge(edge);
            return new MockFactory().createInternal(returnValue, dest);
        } catch (Exception e) {
            ThrowableNode dest = new ThrowableNode(e);
            InternalMethodCall edge = new InternalMethodCall(orignalMethod, source, dest);
            source.addEdge(edge);
            throw e;
        }
    }
}
