package org.parrot.core.listener;

import org.parrot.core.data.objects.Node;
import org.parrot.core.listener.exceptions.InitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectInitializer {
    public static <T> T initRecord(Class<T> clazz,
                                   RecordComponent[] components,
                                   List<Object> params) throws InitializationException {
        try {
            Class<?>[] paramTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class<?>[]::new);
            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            return (T) constructor.newInstance(params.toArray());
        } catch (NoSuchMethodException e) {
            // Can't find constructor for Record
            // should never happen
            throw new InitializationException("No constructor found for record.", e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // unable to create mock
            throw new InitializationException("Constructor Exists, but error while invocation.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) throws InitializationException {
        List<String> causes = new ArrayList<>();
        for (Constructor<?> constructor : clazz.getConstructors()) {
            try {
                int length = constructor.getParameterCount();
                Class<?>[] paramClazzes = constructor.getParameterTypes();
                if (length > 0) {
                    Object[] params = new Object[length];
                    for (int i = 0; i < length; i++) {
                        if (paramClazzes[i].isPrimitive()) {
                            params[i] = 0;
                        } else {
                            params[i] = null;
                        }
                    }
                    return (T) constructor.newInstance(params);
                } else {
                    return (T) constructor.newInstance();
                }
            } catch (Exception e) {
                causes.add(constructor.toGenericString() + " failed with " + e);
            }
        }
        throw new InitializationException("No constructors available");
    }
}
