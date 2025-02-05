package org.rere.core.listener.utils;

import org.rere.core.data.objects.UserNode;

public interface UserObjectSpy extends ObjectSpy{
    UserNode getReReUserNode();
    void setReReUserNode(UserNode node);
    String FIELD = "reReUserNode";
    Class<?> TYPE = UserNode.class;
}
