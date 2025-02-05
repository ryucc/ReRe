package org.rere.core.listener.interceptor;

import java.lang.reflect.Method;

public interface ReReMethodInterceptor<NODE> {

    Object interceptInterface(Object original,
                              Method orignalMethod,
                              NODE sourceNode,
                              Object[] allArguments) throws Throwable;
}
