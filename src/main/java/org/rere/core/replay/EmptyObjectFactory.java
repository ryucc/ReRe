/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

public interface EmptyObjectFactory {
    <T> T getObject(Class<T> clazz);
}
