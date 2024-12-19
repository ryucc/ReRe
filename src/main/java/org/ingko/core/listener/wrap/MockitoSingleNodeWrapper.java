package org.ingko.core.listener.wrap;

import org.ingko.core.listener.interceptor.ParrotMethodInterceptor;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class MockitoSingleNodeWrapper<NODE> implements SingleNodeWrapper<NODE> {
    private final ParrotMethodInterceptor<NODE> listener;
    public MockitoSingleNodeWrapper(ParrotMethodInterceptor<NODE> listener) {
        this.listener = listener;
    }

    @Override
    public <T> T initiateSpied(T returnValue, NODE node) {
        return (T) Mockito.mock(returnValue.getClass(),
                Mockito.withSettings().defaultAnswer(new EnvAns(returnValue, node)).extraInterfaces(EnvironmentObjectSpy.class));
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
            if(method.getName().equals("getParrotOriginObject")) {
                return original;
            } else if(method.getName().equals("getParrotUserNode")) {
                return node;
            }
            return listener.interceptInterface(original, method, node, invocationOnMock.getRawArguments());
        }
    }


}
