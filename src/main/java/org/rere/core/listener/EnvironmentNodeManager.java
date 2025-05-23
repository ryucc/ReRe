/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.api.ReReSettings;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.spies.EnvironmentObjectSpy;
import org.rere.core.serde.ReReSerde;
import org.rere.core.serde.SerdeStrategy;
import org.rere.core.wrap.SingleNodeWrapper;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.exceptions.SerializationException;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides methods for spying on an environment object.
 */
public class EnvironmentNodeManager implements NodeManager<EnvironmentNode> {
    private static final PrimitiveSerde PRIMITIVE_SERDE = new PrimitiveSerde();
    private SingleNodeWrapper<EnvironmentNode> leafNodeWrapper;
    final SerdeStrategy serdeStrategy;

    public EnvironmentNodeManager(ReReMethodInterceptor<EnvironmentNode> listener, ReReSettings reReSettings) {
        //this.leafNodeWrapper = new EnvironmentNodeWrapper(listener);
        //this.wrapper = new JavaProxySingleNodeWrapper<>(listener);
        this.serdeStrategy = new SerdeStrategy(reReSettings.getCustomSerde());
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
    public EnvironmentNode createNull(Class<?> representingClass) {
        return EnvironmentNode.ofNull(representingClass);
    }

    @Override
    public EnvironmentNode createFailed(Class<?> representingClass, String comments) {
        return EnvironmentNode.ofFailed(representingClass, comments);
    }


    public Object synthesizeLeafNode(Object original, EnvironmentNode node) {
        Object wrapped;
        if (serdeStrategy.shouldSerialize(original.getClass())) {
            try {
                ReReSerde serializer = serdeStrategy.getSerializer(original.getClass());
                node.setValue(serializer.serialize(original));
                node.setSerialized(true);
                node.setSerializer(serializer.getClass());
                node.setComments(original.toString());
                node.setTerminal(true);
                wrapped = original;
            } catch (Exception e) {
                node.setTerminal(true);
                node.setFailedNode(true);
                node.setComments("Custom serialization failed: " + e + "\nOriginal content: " + original);
                wrapped = original;
            }
        } else if (ClassUtils.isWrapperOrPrimitive(original.getClass())) {
            node.setValue(original.toString());
            node.setTerminal(true);
            wrapped = original;
        } else if(original.getClass().equals(String.class)) {
            node.setTerminal(true);
            node.setSerialized(true);
            node.setSerializer(PrimitiveSerde.class);
            try {
                node.setValue(PRIMITIVE_SERDE.serialize((String) original));
                node.setComments(original.toString());
            } catch (SerializationException e) {
                node.setFailedNode(true);
                node.setComments(e.getMessage());
            }
            wrapped = original;
        } else if (
                (Modifier.isFinal(original.getClass().getModifiers())
                && Serializable.class.isAssignableFrom(original.getClass()))
                        ||
                Throwable.class.isAssignableFrom(original.getClass())) {
            node.setTerminal(true);
            node.setSerialized(true);
            node.setComments(original.toString());
            node.setSerializer(PrimitiveSerde.class);
            try {
                node.setValue(PRIMITIVE_SERDE.serialize((Serializable) original));
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
