/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.wrap.SingleNodeWrapper;

public class UserNodeManager implements NodeManager<UserNode> {

    private final SingleNodeWrapper<UserNode> wrapper;

    public UserNodeManager(ReReMethodInterceptor<UserNode> listener) {
        //this.wrapper = new UserNodeWrapper(listener);
        this.wrapper = new MockitoSingleNodeWrapper<>(listener, UserObjectSpy.class);
    }

    @Override
    public UserNode createEmpty(Class<?> clazz, Object original) {
        return new UserNode(original.getClass(), clazz);
    }

    @Override
    public UserNode createNull(Class<?> clazz) {
        return new UserNode(clazz, clazz);
    }

    @Override
    public UserNode createFailed(Class<?> clazz, String comments) {
        return new UserNode(clazz, comments);
    }

    public Object synthesizeLeafNode(Object original, UserNode node) {
        if (ClassUtils.isStringOrPrimitive(original.getClass())) {
            return original;
        }
        try {
            return wrapper.initiateSpied(original, node);
        } catch (Exception e) {
            return original;
        }
    }
}
