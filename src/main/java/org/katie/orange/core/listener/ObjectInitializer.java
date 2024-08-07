package org.katie.orange.core.listener;

import java.lang.reflect.Constructor;

public class ObjectInitializer {
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            try {
                int length = constructor.getParameterCount();
                Class<?>[] paramClazzes = constructor.getParameterTypes();
                if (length > 0) {
                    Object[] params = new Object[length];
                    for(int i = 0; i < length; i++) {
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

            }
        }
        return null;
    }
}
