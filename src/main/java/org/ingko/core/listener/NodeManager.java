package org.ingko.core.listener;

public interface NodeManager <SPIED, NODE>{
    NODE createEmpty(Class<?> clazz);
    NODE createNull(Class<?> clazz);
    NODE createFailed(Class<?> clazz, String comments);
    void updateNodePointer(SPIED spied, NODE node);
    void addChild(NODE parent, NODE child);

    Object synthesizeLeafNode(Object original, NODE node);
}
