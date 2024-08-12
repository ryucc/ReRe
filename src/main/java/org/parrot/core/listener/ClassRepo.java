package org.parrot.core.listener;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.parrot.core.listener.exceptions.SubclassingException;

import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ClassRepo {
    private final Object interceptor;
    private final Map<Class<?>, Class<?>> classCache;
    public ClassRepo(Object interceptor) {
        this.interceptor = interceptor;
        this.classCache = new HashMap<>();
    }

    public Class<?> getOrDefineSubclass(Class<?> child, Class<?> target) throws SubclassingException {
        Queue<Class<?>> candidates = new ArrayDeque<>();
        if (target.isAssignableFrom(child)) {
            candidates.add(child);
        } else {
            throw new SubclassingException("Target class is not assignable from input.");
        }
        while (!candidates.isEmpty()) {
            Class<?> cur = candidates.poll();
            System.out.println("cur:" + cur);
            try {
                return getOrDefineSubclass(cur);
            } catch (SubclassingException e) {
                System.out.println("wrong");
            }
            if(cur.getSuperclass()!=null && target.isAssignableFrom(cur.getSuperclass())) {
                candidates.add(cur.getSuperclass());
            }
            Class<?>[] interfaces =  cur.getInterfaces();
            for(Class<?> implementedInterface :interfaces) {
                System.out.println("target:" + target);
                System.out.println("implemented:" + target);
                if(target.isAssignableFrom(implementedInterface)||target.equals(implementedInterface)) {
                    System.out.println("hello:" + target);
                    candidates.add(implementedInterface);
                }
            }
        }
        throw new SubclassingException("other error");
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
            throw new SubclassingException("other error", t);
        }
    }
}
