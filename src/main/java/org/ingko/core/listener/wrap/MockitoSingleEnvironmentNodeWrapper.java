package org.ingko.core.listener.wrap;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public class MockitoSingleEnvironmentNodeWrapper implements SingleNodeWrapper<EnvironmentNode> {
    private final EnvironmentObjectListener listener;
    public MockitoSingleEnvironmentNodeWrapper(EnvironmentObjectListener listener) {
        this.listener = listener;
    }

    @Override
    public <T> T initiateSpied(T returnValue, EnvironmentNode node) {
        return (T) Mockito.mock(returnValue.getClass(),
                Mockito.withSettings().defaultAnswer(new EnvAns(returnValue, node)).extraInterfaces(EnvironmentObjectSpy.class));
    }

    public class EnvAns implements Answer<Object> {

        private final Object original;
        private final EnvironmentNode environmentNode;

        public EnvAns(Object original, EnvironmentNode environmentNode) {
            this.original = original;
            this.environmentNode = environmentNode;
        }

        @Override
        public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
            Method method = invocationOnMock.getMethod();
            return listener.interceptInterface(original, method, environmentNode, invocationOnMock.getRawArguments());
        }
    }


}
