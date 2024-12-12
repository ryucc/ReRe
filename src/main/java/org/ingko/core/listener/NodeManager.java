package org.ingko.core.listener;

public interface NodeManager <NODE>{
    NODE createEmpty(Class<?> clazz);
    NODE createNull(Class<?> clazz);
    NODE createFailed(Class<?> clazz, String comments);
    void addChild(NODE parent, NODE child);

    Object synthesizeLeafNode(Object original, NODE node);
}
