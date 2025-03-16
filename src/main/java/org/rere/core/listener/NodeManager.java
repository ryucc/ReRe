/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.core.data.objects.ReReObjectNode;

public interface NodeManager <NODE extends ReReObjectNode<?>>{
    /**
     * Creates a node representing the original object. No method calls are recorded yet.
     * @param representingClass the representing class of the object.
     * @param original Original object to spy on
     * @return the empty node
     */
    NODE createEmpty(Class<?> representingClass, Object original);

    /**
     * Creates a node representing the null reference.
     * @param representingClass the representing class of the object.
     * @return the node
     */
    NODE createNull(Class<?> representingClass);

    /**
     * Creates a node representing an object that we cannot spy on.
     * @param representingClass the representing class of the object.
     * @param comments Potential reasons of failure.
     * @return the node
     */
    NODE createFailed(Class<?> representingClass, String comments);

    /**
     * Creates a spy of the original object, recording the data on node.
     * TODO: move this method to another class?
     *
     * @param original Original object to spy on.
     * @param node node representing, where runtime data is recorded.
     * @return Wrapped object
     */
    Object synthesizeLeafNode(Object original, NODE node);
}
