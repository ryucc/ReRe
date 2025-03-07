/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects;

import java.util.List;

public interface ReReObjectNode<T> {

    Class<?> getRepresentingClass();
    Class<?> getRuntimeClass();
    void setFailedNode(boolean flag);
    void setComments(String comments);
    void addChild(T child);
    List<? extends ReReObjectNode<T>> getDirectChildren();
}
