package org.katie.orange.core.listener;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.katie.orange.core.data.objects.Node;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Listener {
    RecordInterceptor interceptor;

    public Node getRoot() {
        return roots.get(0);
    }

    List<Node> roots;

    Map<Class<?>, Class<?>> classCache;

    public Listener() {
        roots = new ArrayList<>();
        classCache = new HashMap<>();
        interceptor = new RecordInterceptor(this);
    }

    public <T> T wrap(T original) {
        Node root = new Node(original.getClass());
        roots.add(root);
        return createInternal(original, root);
    }

    @SuppressWarnings("unchecked")
    public <T> T createInternal(T original, Node node) {
        // TODO: while final class, try to mock parent until parent == return class
        Class<?> mockedClass;
        if(classCache.containsKey(original.getClass())) {
            System.out.println("classCache hit");
            mockedClass = classCache.get(original.getClass());
        } else {
            mockedClass = new ByteBuddy()
                    .subclass(original.getClass())
                    .defineField("original", Object.class, Modifier.PUBLIC)
                    .defineField("objectNode", Node.class, Modifier.PUBLIC)
                    .method(ElementMatchers.not(ElementMatchers.isToString()))
                    .intercept(MethodDelegation.to(interceptor))
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            classCache.put(original.getClass(), mockedClass);
        }
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