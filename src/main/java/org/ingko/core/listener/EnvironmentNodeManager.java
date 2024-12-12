package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.core.serde.exceptions.SerializationException;

public class EnvironmentNodeManager implements NodeManager<EnvironmentObjectSpy, EnvironmentNode>{
    private static final DefaultSerde defaultSerde = new DefaultSerde();

    public EnvironmentNodeManager(ClassRepo classRepo) {
        this.classRepo = classRepo;
    }

    private final ClassRepo classRepo;
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
    public void updateNodePointer(EnvironmentObjectSpy spied, EnvironmentNode node) {
        spied.setParrotNodePointer(node);
    }
    public Object synthesizeLeafNode(Object original, EnvironmentNode node){
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
            wrapped = initiateSpied(original, node);
        }
        return wrapped;
    }

    public <T> T initiateSpied(T returnValue, EnvironmentNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            updateNodePointer((EnvironmentObjectSpy) mocked, node);
            ((EnvironmentObjectSpy) mocked).setParrotOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
    }
}
