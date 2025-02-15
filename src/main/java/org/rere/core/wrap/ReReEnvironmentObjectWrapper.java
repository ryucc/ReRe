/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

public interface ReReEnvironmentObjectWrapper<NODE> {
    public <T> ReReWrapResult<T, NODE> createRoot(Object original, Class<?> targetClass);
}
