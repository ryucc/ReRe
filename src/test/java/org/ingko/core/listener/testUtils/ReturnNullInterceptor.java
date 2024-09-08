package org.ingko.core.listener.testUtils;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

public class ReturnNullInterceptor {

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Exception {
        return null;
    }
}
