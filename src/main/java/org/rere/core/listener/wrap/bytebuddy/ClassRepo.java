package org.rere.core.listener.wrap.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.rere.core.listener.exceptions.SubclassingException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.anyOf;
import static net.bytebuddy.matcher.ElementMatchers.not;

/**
 * Handles the subclassing for ReRe Spies. A cache is added to avoid creating a new subclass for each instance of a same class.
 */
public class ClassRepo {
    private final Object interceptor;
    private final Map<Class<?>, Class<?>> classCache;

    private final Map<String, Class<?>> fields;
    private final List<Class<?>> interfaces;

    public ClassRepo(Object interceptor, Map<String, Class<?>> fields, List<Class<?>> interfaces) {
        this.interceptor = interceptor;
        this.classCache = new HashMap<>();
        this.fields = fields;
        this.interfaces = interfaces;
    }


    /**
     * The interceptor is used on every method call, except fillInStackTrace for Exceptions.
     *
     * @param interceptor
     */
    public ClassRepo(Object interceptor, Map<String, Class<?>> fields) {
        this.interceptor = interceptor;
        this.classCache = new HashMap<>();
        this.fields = fields;
        interfaces = List.of();
    }


    public Class<?> getOrDefineSubclass(Class<?> target) throws SubclassingException {
        if (Modifier.isFinal(target.getModifiers())) {
            throw new SubclassingException("Cannot sub final class: " + target.getName());
        }

        ClassLoadingStrategy<ClassLoader> strategy;
        // Trying to sub private classes
        // https://github.com/raphw/byte-buddy/issues/447
        // https://mydailyjava.blogspot.com/2018/04/jdk-11-and-proxies-in-world-past.html
        try {
            if (Modifier.isPublic(target.getModifiers())) {
                strategy = ClassLoadingStrategy.Default.WRAPPER;
            } else if (ClassInjector.UsingLookup.isAvailable()) {
                Class<?> methodHandles = Class.forName("java.lang.invoke.MethodHandles");
                Object lookup = methodHandles.getMethod("lookup").invoke(null);
                Method privateLookupIn = methodHandles.getMethod("privateLookupIn",
                        Class.class,
                        Class.forName("java.lang.invoke.MethodHandles$Lookup"));
                Object privateLookup = privateLookupIn.invoke(null, target, lookup);
                strategy = ClassLoadingStrategy.UsingLookup.of(privateLookup);
            } else if (ClassInjector.UsingReflection.isAvailable()) {
                strategy = ClassLoadingStrategy.Default.INJECTION;
            } else {
                strategy = ClassLoadingStrategy.Default.WRAPPER;
            }
        } catch (Exception e) {
            throw new SubclassingException("Error finding ClassLoadingStrategy.");
        }


        try {
            if (classCache.containsKey(target)) {
                return classCache.get(target);
            }

            List<Method> selfDefinedMethods = interfaces.stream()
                    .flatMap(clazz -> Stream.of(clazz.getMethods()))
                    .toList();

            DynamicType.Builder<?> builder = new ByteBuddy().subclass(target)
                    //TODO: only skip for Throwable
                    .method(anyOf(selfDefinedMethods.toArray()).or(not(ElementMatchers.isToString()).and(not(
                                    ElementMatchers.hasMethodName("fillInStackTrace"))))
                            //.or(ElementMatchers.isHashCode().or(ElementMatchers.isEquals()))
                    ).intercept(MethodDelegation.to(interceptor));
            for (String fieldName : fields.keySet()) {
                builder = builder.defineProperty(fieldName, fields.get(fieldName));
            }
            for (Type type : interfaces) {
                builder = builder.implement(type);
            }

            Class<?> newClass = builder.make().load(getClass().getClassLoader(), strategy).getLoaded();
            classCache.put(target, newClass);
            return newClass;
        } catch (Throwable t) {
            throw new SubclassingException("Cannot subclass", t);
        }
    }
}
