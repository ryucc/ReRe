package org.rere.core.listener.wrap.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.ObjectSpy;
import org.rere.core.listener.wrap.SingleNodeWrapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class EnvironmentNodeWrapper implements SingleNodeWrapper<EnvironmentNode> {
    private final ClassRepo classRepo;

    public EnvironmentNodeWrapper(ReReMethodInterceptor<EnvironmentNode> listener) {
        this.classRepo = new ClassRepo(new Listener(listener),
                Map.of(EnvironmentObjectSpy.FIELD, EnvironmentObjectSpy.TYPE, ObjectSpy.FIELD, ObjectSpy.TYPE),
                List.of(EnvironmentObjectSpy.class, ObjectSpy.class));
    }

    @Override
    public <T> T initiateSpied(T returnValue, EnvironmentNode node) {

        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            ((EnvironmentObjectSpy) mocked).setReReNodePointer(node);
            ((EnvironmentObjectSpy) mocked).setReReOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }

    public static class Listener {

        private final ReReMethodInterceptor<EnvironmentNode> environmentObjectListener;

        public Listener(ReReMethodInterceptor<EnvironmentNode> environmentObjectListener) {
            this.environmentObjectListener = environmentObjectListener;
        }

        @RuntimeType
        public Object intercept(@AllArguments Object[] allArguments,
                                @Origin Method orignalMethod,
                                @This Object self) throws Throwable {
            Object original = ((EnvironmentObjectSpy) self).getReReOriginObject();
            EnvironmentNode source = ((EnvironmentObjectSpy) self).getReReNodePointer();
            return environmentObjectListener.interceptInterface(original, orignalMethod, source, allArguments);
        }
    }
}
