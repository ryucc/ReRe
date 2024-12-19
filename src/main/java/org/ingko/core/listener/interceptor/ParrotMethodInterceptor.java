package org.ingko.core.listener.interceptor;

import org.ingko.core.data.objects.EnvironmentNode;

import java.lang.reflect.Method;

public interface ParrotMethodInterceptor<NODE> {

    Object interceptInterface(Object original,
                              Method orignalMethod,
                              NODE sourceNode,
                              Object[] allArguments) throws Throwable;
}
