package org.ingko.core.listener.utils;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.UserNode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ParrotFieldAccessors {
    public static final String PARROT_USER_NODE = "parrotUserNode";
    public static final String PARROT_ORIGIN_OBJECT_POINTER = "parrotOriginObjectPointer";

    public static final String PARROT_NODE_POINTER = "parrotNodePointer";

    public static boolean isEnvironmentObject(Object object) {
        if (object == null) {
            return false;
        }
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getFields()) {
            if (field.getName().equals(PARROT_NODE_POINTER)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isUserObject(Object object) {
        if (object == null) {
            return false;
        }
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getFields()) {
            if (field.getName().equals(PARROT_USER_NODE)) {
                return true;
            }
        }
        return false;
    }

    public static EnvironmentNode getNode(Object self) {
        try {
            return (EnvironmentNode) self.getClass().getMethod("getParrotNodePointer").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getNode on a non wrapped object.");
        }
    }

    public static UserNode getUserNode(Object self) {
        try {
            return (UserNode) self.getClass().getMethod("getParrotUserNode").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getScope on a non wrapped object.");
        }
    }

    public static void setUserNode(Object mocked, UserNode node) {
        try {
            mocked.getClass().getMethod("setParrotUserNode", UserNode.class).invoke(mocked, node);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking setScope on a non wrapped object.");
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

    public static void setNode(Object mocked, EnvironmentNode environmentNode) {
        try {
            mocked.getClass().getMethod("setParrotNodePointer", EnvironmentNode.class).invoke(mocked, environmentNode);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getOriginal on a non wrapped object.");
        }
    }
}
