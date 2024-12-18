package org.ingko.core.replay;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.wrap.bytebuddy.ClassRepo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class InOrderReplayUtils {
    public static EmptyObjectFactory getFactory(InOrderReplayListener listener) {
        ClassRepo classRepo = new ClassRepo(listener, Map.of());
        return null;
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

    public static EnvironmentNode getNode(Object self) {
        try {
            return (EnvironmentNode) self.getClass().getMethod("getParrotNodePointer").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the parrot pointer fields were not properlly created.
            throw new RuntimeException("Invoking getNode on a non wrapped object.");
        }
    }
}
