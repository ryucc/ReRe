/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener;

import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.spies.UserObjectSpy;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;
import org.rere.core.wrap.SingleNodeWrapper;

public class UserNodeManager implements NodeManager<UserNode> {

    private final SingleNodeWrapper<UserNode> wrapper;

    public UserNodeManager(ReReMethodInterceptor<UserNode> listener) {
        //this.wrapper = new UserNodeWrapper(listener);
        this.wrapper = new MockitoSingleNodeWrapper<>(listener, UserObjectSpy.class);
    }

    @Override
    public UserNode createEmpty(Class<?> representingClass, Object original) {
        return new UserNode(original.getClass(), representingClass);
    }

    @Override
    public UserNode createNull(Class<?> representingClass) {
        return new UserNode(representingClass, representingClass);
    }

    @Override
    public UserNode createFailed(Class<?> representingClass, String comments) {
        return new UserNode(representingClass, comments);
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
