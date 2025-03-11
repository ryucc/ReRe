/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.spies;

public interface ObjectSpy {
    void setReReOriginObject(Object original);
    Object getReReOriginObject();
    String FIELD = "reReOriginObject";
    Class<?> TYPE = Object.class;
}
