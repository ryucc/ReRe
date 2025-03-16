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
import org.rere.core.listener.spies.EnvironmentObjectSpy;
import org.rere.core.listener.spies.ObjectSpy;
import org.rere.core.wrap.SingleNodeWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Bytebuddy implementation for spying on environment objects.
 */
public class EnvironmentNodeWrapper implements SingleNodeWrapper<EnvironmentNode> {
    private final ClassRepo classRepo;

    /**
     *  Constructor.
     * @param listener Provides the interceptor with the mock object is invoked.
     */
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

    /**
     * Creates a spied version of the original object.
     *
     * @param originalObject Object to spy on.
     * @param node Node to record data on.
     * @return spied version of the original object.
     */
    @Override
    public Object initiateSpied(Object originalObject, EnvironmentNode node) {

        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(originalObject.getClass());
            Object mocked = ObjectInitializer.create(mockedClass);
            ((EnvironmentObjectSpy) mocked).setReReNodePointer(node);
            ((EnvironmentObjectSpy) mocked).setReReOriginObject(originalObject);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            node.setComments(e.getMessage());
            return originalObject;
        }
    }

    /**
     * Implements the interception interface for bytebuddy method interception.
     */
    public static class Listener {

        private final ReReMethodInterceptor<EnvironmentNode> environmentObjectListener;

        /**
         * Constructor.
         *
         * @param environmentObjectListener Provides the interceptor with the mock object is invoked.
         */
        public Listener(ReReMethodInterceptor<EnvironmentNode> environmentObjectListener) {
            this.environmentObjectListener = environmentObjectListener;
        }

        /**
         * Implementation of byteBuddy interception interface.
         *
         * @param allArguments arguments provided to the method call.
         * @param orignalMethod the method invoked, belonging to the superclass?
         * @param self the object owning the invoked method.
         * @return Maybe a spied version of the original return object.
         * @throws Throwable any exception thrown by the method
         */
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
