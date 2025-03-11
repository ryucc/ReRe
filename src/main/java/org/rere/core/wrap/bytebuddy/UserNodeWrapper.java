/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.spies.ObjectSpy;
import org.rere.core.listener.spies.UserObjectSpy;
import org.rere.core.wrap.SingleNodeWrapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class UserNodeWrapper implements SingleNodeWrapper<UserNode> {
    private final ClassRepo classRepo;

    public UserNodeWrapper(ReReMethodInterceptor<UserNode> listener) {
        classRepo = new ClassRepo(new Listener(listener),
                Map.of(ObjectSpy.FIELD, ObjectSpy.TYPE, UserObjectSpy.FIELD, UserObjectSpy.TYPE),
                List.of(ObjectSpy.class, UserObjectSpy.class));
    }

    @Override
    public Object initiateSpied(Object returnValue, UserNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            Object mocked = ObjectInitializer.create(mockedClass);
            ((UserObjectSpy) mocked).setReReUserNode(node);
            ((UserObjectSpy) mocked).setReReOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }

    public static class Listener {

        private final ReReMethodInterceptor<UserNode> userObjectListener;

        public Listener(ReReMethodInterceptor<UserNode> userObjectListener) {
            this.userObjectListener = userObjectListener;
        }

        @RuntimeType
        public Object intercept(@AllArguments Object[] allArguments,
                                @Origin Method orignalMethod,
                                @This Object self) throws Throwable {
            Object original = ((UserObjectSpy) self).getReReOriginObject();
            UserNode source = ((UserObjectSpy) self).getReReUserNode();
            return userObjectListener.interceptInterface(original, orignalMethod, source, allArguments);
        }
    }
}
