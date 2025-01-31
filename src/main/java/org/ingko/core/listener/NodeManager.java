package org.ingko.core.listener;

public interface NodeManager <NODE>{
    NODE createEmpty(Class<?> clazz, Object original);
    NODE createNull(Class<?> clazz);
    NODE createFailed(Class<?> clazz, String comments);
    Object synthesizeLeafNode(Object original, NODE node);
}
