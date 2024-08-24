import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ByteBuddyInterceptorExample {

    public static class LoggingInterceptor {
        @RuntimeType
        public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
            System.out.println("Before method: " + method.getName());
            try {
                return callable.call();
            } finally {
                System.out.println("After method: " + method.getName());
            }
        }
    }

    public static class OriginalClass {
        public void doSomething() {
            System.out.println("Doing something");
        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(OriginalClass.class)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(LoggingInterceptor.class))
                .make()
                .load(ByteBuddyInterceptorExample.class.getClassLoader())
                .getLoaded();

        OriginalClass instance = (OriginalClass) dynamicType.getDeclaredConstructor().newInstance();
        instance.doSomething();
    }
}
