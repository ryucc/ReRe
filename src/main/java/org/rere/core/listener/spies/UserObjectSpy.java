/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.spies;

import org.rere.core.data.objects.UserNode;

public interface UserObjectSpy extends ObjectSpy{
    UserNode getReReUserNode();
    void setReReUserNode(UserNode node);
    String FIELD = "reReUserNode";
    Class<?> TYPE = UserNode.class;
}
