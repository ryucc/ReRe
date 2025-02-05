package org.rere.core.data.objects;

public interface ReReObjectNode<T> {
    void setFailedNode(boolean flag);
    void setComments(String comments);
    void addChild(T child);
}
