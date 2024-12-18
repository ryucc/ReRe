package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.wrap.ByteBuddySingleEnvironmentNodeWrapper;
import org.ingko.core.listener.wrap.MockitoSingleEnvironmentNodeWrapper;
import org.ingko.core.listener.wrap.SingleNodeWrapper;
import org.ingko.core.listener.wrap.bytebuddy.ClassRepo;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.core.serde.exceptions.SerializationException;

public class EnvironmentNodeManager implements NodeManager<EnvironmentNode> {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    private final SingleNodeWrapper<EnvironmentNode> wrapper;
    //private final MockitoSingleEnvironmentNodeWrapper wrapper;

    public EnvironmentNodeManager(EnvironmentObjectListener listener) {
        //this.wrapper = new ByteBuddySingleEnvironmentNodeWrapper(listener);
        this.wrapper = new MockitoSingleEnvironmentNodeWrapper(listener);
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

    public void addChild(EnvironmentNode parent, EnvironmentNode child) {
        parent.addDirectChild(child);
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
            wrapped = wrapper.initiateSpied(original, node);
        }
        return wrapped;
    }
}
