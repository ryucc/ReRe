/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.interceptor;

import java.lang.reflect.Method;

public interface ReReMethodInterceptor<NODE> {

    Object interceptInterface(Object original,
                              Method orignalMethod,
                              NODE sourceNode,
                              Object[] allArguments) throws Throwable;
}
