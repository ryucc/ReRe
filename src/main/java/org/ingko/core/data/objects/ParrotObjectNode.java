package org.ingko.core.data.objects;

public interface ParrotObjectNode<T> {
    void setFailedNode(boolean flag);
    void setComments(String comments);
    void addChild(T child);
}
