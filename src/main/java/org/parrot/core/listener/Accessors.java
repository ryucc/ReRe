package org.parrot.core.listener;

import org.parrot.core.data.objects.Node;

import java.lang.reflect.InvocationTargetException;

public class Accessors {
    public static Node getNode(Object self) {
        try {
            return (Node) self.getClass().getMethod("getParrotNodePointer").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getNode on a non wrapped object.");
        }
    }

    public static Object getOriginal(Object self) {
        try {
            return self.getClass().getMethod("getParrotOriginObjectPointer").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getOriginal on a non wrapped object.");
        }
    }

    public static void setOriginal(Object mocked, Object original) {
        try {
            mocked.getClass().getMethod("setParrotOriginObjectPointer", Object.class).invoke(mocked, original);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getOriginal on a non wrapped object.");
        }
    }

    public static void setNode(Object mocked, Node node) {
        try {
            mocked.getClass().getMethod("setParrotNodePointer", Node.class).invoke(mocked, node);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getOriginal on a non wrapped object.");
        }
    }
}
