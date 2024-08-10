package org.katie.orange.core.listener;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.katie.orange.core.listener.exceptions.SubclassingException;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ClassRepo {
    private final Object interceptor;
    private final Map<Class<?>, Class<?>> classCache;
    public ClassRepo(Object interceptor) {
        this.interceptor = interceptor;
        this.classCache = new HashMap<>();
    }

    public Class<?> getOrDefineSubclass(Class<?> target) throws SubclassingException {
        if (Modifier.isFinal(target.getModifiers())) {
            throw new SubclassingException("Cannot sub final class: " + target.getName());
        } else if(Modifier.isPrivate(target.getModifiers())) {
            throw new SubclassingException("Cannot sub private class: " + target.getName());
        }
        try {
            if (classCache.containsKey(target)) {
                return classCache.get(target);
            }
            Class<?> newClass = new ByteBuddy().subclass(target)
                    .method(ElementMatchers.not(ElementMatchers.isToString()
                            .or(ElementMatchers.isHashCode().or(ElementMatchers.isEquals()))))
                    .intercept(MethodDelegation.to(interceptor))
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            classCache.put(target, newClass);
            return newClass;
        } catch (Throwable t) {
            throw new SubclassingException("No constructors available", t);
        }
    }
}
