package org.rere.core.replay;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.wrap.bytebuddy.ClassRepo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class InOrderReplayUtils {
    public static EmptyObjectFactory getFactory(InOrderReplayListener listener) {
        ClassRepo classRepo = new ClassRepo(listener, Map.of());
        return null;
    }



    public static void setNode(Object mocked, EnvironmentNode environmentNode) {
        try {
            mocked.getClass().getMethod("setReReNodePointer", EnvironmentNode.class).invoke(mocked, environmentNode);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the ReRe pointer fields were not properlly created.
            throw new RuntimeException("Invoking getOriginal on a non wrapped object.");
        }
    }

    public static EnvironmentNode getNode(Object self) {
        try {
            return (EnvironmentNode) self.getClass().getMethod("getReReNodePointer").invoke(self);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // Very bad exception. This means intercepted on wrapper object,
            // or the ReRe pointer fields were not properlly created.
            throw new RuntimeException("Invoking getNode on a non wrapped object.");
        }
    }
}
