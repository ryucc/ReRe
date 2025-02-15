/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.core.data.objects.ReReObjectNode;

public interface NodeManager <NODE extends ReReObjectNode<?>>{
    NODE createEmpty(Class<?> clazz, Object original);
    NODE createNull(Class<?> clazz);
    NODE createFailed(Class<?> clazz, String comments);
    Object synthesizeLeafNode(Object original, NODE node);
}
