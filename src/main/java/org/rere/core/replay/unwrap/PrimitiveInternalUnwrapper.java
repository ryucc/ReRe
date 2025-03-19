/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.utils.ClassUtils;

public class PrimitiveInternalUnwrapper implements ReplayInternalUnwrapper {
    public Object parsePrimitive(Class<?> clazz, String value) {
        if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.valueOf(value);
        } else if(clazz.equals(Character.class) || clazz.equals(char.class)) {
            return value.charAt(0);
        } else if(clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return Byte.valueOf(value);
        } else if(clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.valueOf(value);
        } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(value);
        } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return Boolean.valueOf(value);
        } else if(clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.valueOf(value);
        } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(value);
        }
        return null;
    }

    @Override
    public Object unwrap(EnvironmentNode node) {
        return parsePrimitive(node.getRuntimeClass(), node.getValue());
    }

    @Override
    public boolean accept(EnvironmentNode node) {
        return ClassUtils.isWrapperOrPrimitive(node.getRuntimeClass());
    }
}
