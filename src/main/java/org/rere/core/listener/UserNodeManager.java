package org.rere.core.listener;

import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.listener.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.listener.wrap.SingleNodeWrapper;

public class UserNodeManager implements NodeManager<UserNode> {

    private final SingleNodeWrapper<UserNode> wrapper;

    public UserNodeManager(ReReMethodInterceptor<UserNode> listener) {
        //this.wrapper = new ByteBuddyUserNodeWrapper(listener);
        this.wrapper = new MockitoSingleNodeWrapper<>(listener, UserObjectSpy.class);
    }

    @Override
    public UserNode createEmpty(Class<?> clazz, Object original) {
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
