/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.api.ReReSettings;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.serde.ReReSerde;
import org.rere.core.wrap.SingleNodeWrapper;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.exceptions.SerializationException;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Map;

public class EnvironmentNodeManager implements NodeManager<EnvironmentNode> {
    private static final PrimitiveSerde PRIMITIVE_SERDE = new PrimitiveSerde();
    private SingleNodeWrapper<EnvironmentNode> leafNodeWrapper;
    final Map<Class<?>, Class<? extends ReReSerde>> customSerde;

    public EnvironmentNodeManager(ReReMethodInterceptor<EnvironmentNode> listener, ReReSettings reReSettings) {
        //this.leafNodeWrapper = new EnvironmentNodeWrapper(listener);
        //this.wrapper = new JavaProxySingleNodeWrapper<>(listener);
        this.customSerde = reReSettings.getCustomSerde();
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
        if (customSerde.containsKey(original.getClass())) {
            try {
                Class<? extends ReReSerde> serializer = customSerde.get(original.getClass());
                ReReSerde reReSerde = serializer.getConstructor().newInstance();
                node.setValue(reReSerde.serialize(original));
                node.setSerialized(true);
                node.setSerializer(customSerde.get(original.getClass()));
                node.setTerminal(true);
                wrapped = original;
            } catch (Exception e) {
                node.setTerminal(true);
                node.setFailedNode(true);
                node.setComments("Custom serialization failed: " + e);
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
                node.setValue(PRIMITIVE_SERDE.serialize(original));
                node.setComments((String) original);
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
            node.setSerializer(PrimitiveSerde.class);
            try {
                node.setValue(PRIMITIVE_SERDE.serialize(original));
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
