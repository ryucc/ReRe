/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.ObjectSpy;
import org.rere.core.wrap.SingleNodeWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentNodeWrapper implements SingleNodeWrapper<EnvironmentNode> {
    private final ClassRepo classRepo;

    public EnvironmentNodeWrapper(ReReMethodInterceptor<EnvironmentNode> listener) {
        this.classRepo = new ClassRepo(new Listener(listener),
                initClassSpecMap(),
                Arrays.asList(EnvironmentObjectSpy.class, ObjectSpy.class));
    }

    private static Map<String, Class<?>> initClassSpecMap() {
        Map<String, Class<?>> spec = new HashMap<>();
        spec.put(EnvironmentObjectSpy.FIELD, EnvironmentObjectSpy.TYPE);
        spec.put(ObjectSpy.FIELD, ObjectSpy.TYPE);
        return spec;
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
            node.setComments(e.getMessage());
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
