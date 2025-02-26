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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isFinal;

@SuppressWarnings("unchecked")
public class MockitoSingleNodeWrapper<NODE extends ReReObjectNode<?>> implements SingleNodeWrapper<NODE> {
    private final ReReMethodInterceptor<NODE> listener;
    private final Class<?> extraInterface;

    public MockitoSingleNodeWrapper(ReReMethodInterceptor<NODE> listener, Class<?> extraInterface) {
        this.listener = listener;
        this.extraInterface = extraInterface;
    }
    public List<Class<?>> findBestClass(Class<?> start, Class<?> end) {
        if(CompletionStage.class.isAssignableFrom(start)) {
            return new ArrayList<>();
        }
        if(!end.isAssignableFrom(start)) {
            return new ArrayList<>();
        }
        List<Class<?>> ans = new ArrayList<>();

        int mod = start.getModifiers();
        if(Modifier.isPublic(mod) && !isFinal(mod)) {
            ans.add(start);
        }
        if(start.getSuperclass() != null && end.isAssignableFrom(start.getSuperclass())){
            Class<?> sup = start.getSuperclass();
            int supMod = sup.getModifiers();
            if(Modifier.isPublic(supMod) && !isFinal(supMod)) {
                ans.add(sup);
            }
        }
        for(Class<?> next: start.getInterfaces()) {
            if(next.getMethods().length == 0) continue;
            if(end.isAssignableFrom(next)) {
                ans.add(next);
            }
        }
        if(!ans.contains(end) && !end.equals(Object.class)) {
            ans.add(end);
        }
        return ans;
    }

    public Object failFinal(Object returnValue, NODE node) {
        node.setComments("ReRe cannot spy on final class: " + node.getRepresentingClass()
        + "\n" + "Further method tracing maybe incorrect on this object."
        + "\n" + "If this is a environment object, consider using custom serialization.");
        node.setFailedNode(true);
        return returnValue;
    }
    @Override
    public Object initiateSpied(Object returnValue, NODE node) {
        if(isFinal(node.getRepresentingClass().getModifiers())) {
            return failFinal(returnValue, node);
        }
        List<Class<?>> classes = findBestClass(node.getRuntimeClass(), node.getRepresentingClass());
        List<Exception> failReasons = new ArrayList<>();

        for(Class<?> bestClass: classes) {
            try {
                return Mockito.mock(bestClass,
                        Mockito.withSettings()
                                .defaultAnswer(new EnvAns(returnValue, node))
                                .extraInterfaces(extraInterface));
            } catch (Exception e) {
                failReasons.add(e);
            }
        }
        node.setFailedNode(true);
        node.setComments(failReasons.stream().map(Throwable::toString).collect(Collectors.joining("\n")));
        return returnValue;
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
