package org.ingko.core.listener.wrap.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.ObjectInitializer;
import org.ingko.core.listener.interceptor.ParrotMethodInterceptor;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.listener.utils.UserObjectSpy;
import org.ingko.core.listener.wrap.SingleNodeWrapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class UserNodeWrapper implements SingleNodeWrapper<UserNode> {
    private final ClassRepo classRepo;

    public UserNodeWrapper(ParrotMethodInterceptor<UserNode> listener) {
        classRepo = new ClassRepo(new Listener(listener),
                Map.of(ObjectSpy.FIELD, ObjectSpy.TYPE, UserObjectSpy.FIELD, UserObjectSpy.TYPE),
                List.of(ObjectSpy.class, UserObjectSpy.class));
    }

    @Override
    public <T> T initiateSpied(T returnValue, UserNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            ((UserObjectSpy) mocked).setParrotUserNode(node);
            ((UserObjectSpy) mocked).setParrotOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }

    public static class Listener {

        private final ParrotMethodInterceptor<UserNode> userObjectListener;

        public Listener(ParrotMethodInterceptor<UserNode> userObjectListener) {
            this.userObjectListener = userObjectListener;
        }

        @RuntimeType
        public Object intercept(@AllArguments Object[] allArguments,
                                @Origin Method orignalMethod,
                                @This Object self) throws Throwable {
            Object original = ((UserObjectSpy) self).getParrotOriginObject();
            UserNode source = ((UserObjectSpy) self).getParrotUserNode();
            return userObjectListener.interceptInterface(original, orignalMethod, source, allArguments);
        }
    }
}
