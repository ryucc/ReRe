package org.ingko.core.listener.utils;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.UserNode;

public interface UserObjectSpy extends ObjectSpy{
    UserNode getParrotUserNode();
    void setParrotUserNode(UserNode node);
    String FIELD = "parrotUserNode";
    Class<?> TYPE = UserNode.class;
}
