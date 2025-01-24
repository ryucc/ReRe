package org.ingko.core.listener.wrap.javaproxy;

import org.ingko.core.listener.interceptor.ParrotMethodInterceptor;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.wrap.SingleNodeWrapper;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * This implementation only works for interfaces.
 */
@SuppressWarnings("unchecked")
public class JavaProxySingleNodeWrapper<NODE> implements SingleNodeWrapper<NODE> {
    private final ParrotMethodInterceptor<NODE> listener;
    public JavaProxySingleNodeWrapper(ParrotMethodInterceptor<NODE> listener) {
        this.listener = listener;
    }

    @Override
    public <T> T initiateSpied(T original, NODE node) {
        try {
            Class<?> proxyClass = Proxy.getProxyClass(original.getClass().getClassLoader(), original.getClass());
            return (T) proxyClass.getConstructor(InvocationHandler.class).
                    newInstance(new MyInvocationHandler(original, node));
        } catch (Exception e) {
            return original;
        }
    }

    class MyInvocationHandler implements InvocationHandler {
        private final Object original;
        private final NODE node;

        public MyInvocationHandler(Object returnValue, NODE node) {
            this.original = returnValue;
            this.node = node;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            /*
                TODO: code smell.
                This piece only applies to UserNodes
             */
            if(method.getName().equals("getParrotOriginObject")) {
                return original;
            } else if(method.getName().equals("getParrotUserNode")) {
                return node;
            }
            return listener.interceptInterface(original, method, node, args);
        }
    }


}
