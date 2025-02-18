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

    public Class<?> findBestClass(Class<?> start, Class<?> end) {
        if(!end.isAssignableFrom(start)) {
            return null;
        }

        int mod = start.getModifiers();
        if(Modifier.isPublic(mod) && !Modifier.isFinal(mod)) {
            return start;
        } else if(start.getSuperclass() != null && end.isAssignableFrom(start.getSuperclass())){
            Class<?> sup = start.getSuperclass();
            int supMod = sup.getModifiers();
            if(Modifier.isPublic(supMod) && !Modifier.isFinal(supMod)) {
                return sup;
            }
        }
        for(Class<?> next: start.getInterfaces()) {
            if(end.isAssignableFrom(next)) {
                return next;
            }
        }
        return null;
    }

    @Override
    public Object initiateSpied(Object returnValue, NODE node) {
        Class<?> bestClass = findBestClass(returnValue.getClass(), node.getRepresentingClass());
        if(bestClass == null) {
            node.setFailedNode(true);
            node.setComments("Cannot find proper class to mock");
            return returnValue;
        }
        try {
            return Mockito.mock(bestClass,
                    Mockito.withSettings()
                            .defaultAnswer(new EnvAns(returnValue, node))
                            .extraInterfaces(extraInterface));
        } catch (Exception e) {
            return returnValue;
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
