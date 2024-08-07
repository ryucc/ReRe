package org.katie.orange.core;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.katie.orange.core.data.objects.Node;

import java.lang.reflect.Modifier;


public class MockFactory {
    RecordInterceptor interceptor;

    public MockFactory() {
        interceptor = new RecordInterceptor(this);
    }

    public <T> T createRoot(T original) {
        Node node = new Node(original.getClass());
        return createInternal(original, node);
    }

    @SuppressWarnings("unchecked")
    public <T> T createInternal(T original, Node node) {
        Class<?> mockedClass = new ByteBuddy()
                .subclass(original.getClass())
                .defineField("original", Object.class, Modifier.PUBLIC)
                .defineField("objectNode", Node.class, Modifier.PUBLIC)
                .method(ElementMatchers.not(ElementMatchers.isToString()))
                .intercept(MethodDelegation.to(interceptor))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        try {
            T mocked = (T) ObjectInitializer.create(mockedClass);
            mockedClass.getDeclaredField("original").set(mocked, original);
            mockedClass.getDeclaredField("objectNode").set(mocked, node);
            return mocked;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}