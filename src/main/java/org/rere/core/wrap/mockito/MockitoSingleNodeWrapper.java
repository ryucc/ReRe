/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap.mockito;

import org.rere.core.data.objects.ReReObjectNode;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.wrap.SingleNodeWrapper;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
public class MockitoSingleNodeWrapper<NODE extends ReReObjectNode<?>> implements SingleNodeWrapper<NODE> {
    private final ReReMethodInterceptor<NODE> listener;
    private final Class<?> extraInterface;

    public MockitoSingleNodeWrapper(ReReMethodInterceptor<NODE> listener, Class<?> extraInterface) {
        this.listener = listener;
        this.extraInterface = extraInterface;
    }

    @Override
    public Object initiateSpied(Object returnValue, NODE node) {
        try {
            return Mockito.mock(returnValue.getClass(),
                    Mockito.withSettings()
                            .defaultAnswer(new EnvAns(returnValue, node))
                            .extraInterfaces(extraInterface));
        } catch (Exception e) {
            return Mockito.mock(node.getRepresentingClass(),
                    Mockito.withSettings()
                            .defaultAnswer(new EnvAns(returnValue, node))
                            .extraInterfaces(extraInterface));
        }
    }

    /*
    TODO: make this class static
     */
    public class EnvAns implements Answer<Object> {

        private final Object original;
        private final NODE node;

        public EnvAns(Object original, NODE node) {
            this.original = original;
            this.node = node;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            Method method = invocationOnMock.getMethod();
            /*
                TODO: code smell.
                This piece only applies to UserNodes
             */
            if (method.getName().equals("getReReOriginObject")) {
                return original;
            } else if (method.getName().equals("getReReUserNode")) {
                return node;
            }
            return listener.interceptInterface(original, method, node, invocationOnMock.getRawArguments());
        }
    }


}
