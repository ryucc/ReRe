package org.ingko.core.listener.wrap;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.listener.ObjectInitializer;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.listener.wrap.bytebuddy.ClassRepo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ByteBuddySingleEnvironmentNodeWrapper implements SingleNodeWrapper<EnvironmentNode> {
    private final ClassRepo classRepo;

    public ByteBuddySingleEnvironmentNodeWrapper(EnvironmentObjectListener listener) {
        this.classRepo = new ClassRepo(new Listener(listener),
                Map.of(EnvironmentObjectSpy.FIELD, EnvironmentObjectSpy.TYPE, ObjectSpy.FIELD, ObjectSpy.TYPE),
                List.of(EnvironmentObjectSpy.class, ObjectSpy.class));
    }

    @Override
    public <T> T initiateSpied(T returnValue, EnvironmentNode node) {

        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            ((EnvironmentObjectSpy) mocked).setParrotNodePointer(node);
            ((EnvironmentObjectSpy) mocked).setParrotOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }

    public static class Listener {

        private final EnvironmentObjectListener environmentObjectListener;

        public Listener(EnvironmentObjectListener environmentObjectListener) {
            this.environmentObjectListener = environmentObjectListener;
        }

        @RuntimeType
        public Object intercept(@AllArguments Object[] allArguments,
                                @Origin Method orignalMethod,
                                @This Object self) throws Throwable {
            Object original = ((EnvironmentObjectSpy) self).getParrotOriginObject();
            EnvironmentNode source = ((EnvironmentObjectSpy) self).getParrotNodePointer();
            return environmentObjectListener.interceptInterface(original, orignalMethod, source, allArguments);
        }
    }
}
