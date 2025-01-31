package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.ParrotMethodInterceptor;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.wrap.SingleNodeWrapper;
import org.ingko.core.listener.wrap.bytebuddy.EnvironmentNodeWrapper;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.core.serde.exceptions.SerializationException;

public class EnvironmentNodeManager implements NodeManager<EnvironmentNode> {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    private SingleNodeWrapper<EnvironmentNode> leafNodeWrapper;

    public EnvironmentNodeManager(ParrotMethodInterceptor<EnvironmentNode> listener) {
        this.leafNodeWrapper = new EnvironmentNodeWrapper(listener);
        //this.wrapper = new JavaProxySingleNodeWrapper<>(listener);
        //this.wrapper = new MockitoSingleNodeWrapper<>(listener, EnvironmentObjectSpy.class);
    }
    //private final MockitoSingleEnvironmentNodeWrapper wrapper;

    public void setLeafNodeWrapper(SingleNodeWrapper<EnvironmentNode> leafNodeWrapper) {
        this.leafNodeWrapper = leafNodeWrapper;
    }

    @Override
    public EnvironmentNode createEmpty(Class<?> clazz) {
        return EnvironmentNode.ofInternal(clazz);
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
        if (ClassUtils.isString(original.getClass())) {
            node.setValue("\"" + original + "\"");
            node.setTerminal(true);
            wrapped = original;
        } else if (ClassUtils.isStringOrPrimitive(original.getClass())) {
            node.setValue(original.toString());
            node.setTerminal(true);
            wrapped = original;
        } else if (Throwable.class.isAssignableFrom(original.getClass())) {
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
