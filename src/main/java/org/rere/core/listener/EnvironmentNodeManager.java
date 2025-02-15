/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.wrap.SingleNodeWrapper;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.serde.DefaultSerde;
import org.rere.core.serde.exceptions.SerializationException;

import java.io.Serializable;
import java.lang.reflect.Modifier;

public class EnvironmentNodeManager implements NodeManager<EnvironmentNode> {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    private SingleNodeWrapper<EnvironmentNode> leafNodeWrapper;

    public EnvironmentNodeManager(ReReMethodInterceptor<EnvironmentNode> listener) {
        //this.leafNodeWrapper = new EnvironmentNodeWrapper(listener);
        //this.wrapper = new JavaProxySingleNodeWrapper<>(listener);
        this.leafNodeWrapper = new MockitoSingleNodeWrapper<>(listener, EnvironmentObjectSpy.class);
    }
    //private final MockitoSingleEnvironmentNodeWrapper wrapper;

    public void setLeafNodeWrapper(SingleNodeWrapper<EnvironmentNode> leafNodeWrapper) {
        this.leafNodeWrapper = leafNodeWrapper;
    }

    @Override
    public EnvironmentNode createEmpty(Class<?> representingClass, Object original) {
        if(ClassUtils.isStringOrPrimitive(representingClass)) {
            return EnvironmentNode.ofPrimitive(representingClass, original.toString());
        }
        return EnvironmentNode.ofInternal(original.getClass(), representingClass);
    }

    @Override
    public EnvironmentNode createNull(Class<?> clazz) {
        return EnvironmentNode.ofNull(clazz);
    }

    @Override
    public EnvironmentNode createFailed(Class<?> clazz, String comments) {
        return EnvironmentNode.ofFailed(clazz, comments);
    }


    public Object synthesizeLeafNode(Object original, EnvironmentNode node) {
        Object wrapped;
        if (ClassUtils.isWrapperOrPrimitive(original.getClass())) {
            node.setValue(original.toString());
            node.setTerminal(true);
            wrapped = original;
        } else if (
                (Modifier.isFinal(original.getClass().getModifiers())
                && Serializable.class.isAssignableFrom(original.getClass()))
                        ||
                Throwable.class.isAssignableFrom(original.getClass())) {
            node.setTerminal(true);
            node.setSerialized(true);
            try {
                node.setValue(defaultSerde.serialize(original));
            } catch (SerializationException e) {
                node.setFailedNode(true);
                node.setComments(e.getMessage());
            }
            wrapped = original;
        } else {
            wrapped = leafNodeWrapper.initiateSpied(original, node);
        }
        return wrapped;
    }
}
