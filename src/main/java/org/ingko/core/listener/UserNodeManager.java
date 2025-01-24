package org.ingko.core.listener;

import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.interceptor.ParrotMethodInterceptor;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.utils.UserObjectSpy;
import org.ingko.core.listener.wrap.MockitoSingleNodeWrapper;
import org.ingko.core.listener.wrap.SingleNodeWrapper;

public class UserNodeManager implements NodeManager<UserNode> {

    private final SingleNodeWrapper<UserNode> wrapper;

    public UserNodeManager(ParrotMethodInterceptor<UserNode> listener) {
        //this.wrapper = new ByteBuddyUserNodeWrapper(listener);
        this.wrapper = new MockitoSingleNodeWrapper<>(listener, UserObjectSpy.class);
    }

    @Override
    public UserNode createEmpty(Class<?> clazz) {
        return new UserNode(clazz);
    }

    @Override
    public UserNode createNull(Class<?> clazz) {
        return new UserNode(clazz);
    }

    @Override
    public UserNode createFailed(Class<?> clazz, String comments) {
        return new UserNode(clazz, comments);
    }

    public void addChild(UserNode parent, UserNode child) {
        parent.addDirectChild(child);
    }


    public Object synthesizeLeafNode(Object original, UserNode node) {
        Object wrapped;
        if (ClassUtils.isStringOrPrimitive(original.getClass())) {
            wrapped = original;
        } else {
            wrapped = wrapper.initiateSpied(original, node);
        }
        return wrapped;
    }
}
