package org.rere;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isFinal;

public class MyTest {

    interface HelloWorld {
        public void greet();
        public void greetSomeone(String someone);
    }


    @Test
    public void test() throws Exception {
        MyClass<List<Integer>> myClass = new MyClass<>(new ArrayList<>());
        List<Integer> list = new ArrayList<>();

        Method m1 = myClass.getClass().getMethod("getObj" );
        Method m2 = myClass.getClass().getMethod("getOne" );
        Type t1 = m1.getGenericReturnType();
        //InvocationOnMock
        System.out.println(t1);
        Class<?> c1 = m1.getReturnType();
        Type t2 = m2.getGenericReturnType();
        Class<?> c2 = m2.getReturnType();

        HelloWorld spanishGreeting = new HelloWorld() {
            String name = "mundo";
            public void greet() {
                greetSomeone("mundo");
            }
            public void greetSomeone(String someone) {
                name = someone;
                System.out.println("Hola, " + name);
            }
        };
        Class<?> A = spanishGreeting.getClass();
        HelloWorld mockA = (HelloWorld) Mockito.mock(A);

        boolean b = isFinal(A.getModifiers());
        Constructor[] Ac = A.getConstructors();
        return;

    }

    public class MyClass<T extends List<?>> {

        private final T obj;

        public MyClass(T obj) {
            this.obj = obj;
        }

        public T getObj() {
            return obj;
        }
        public List<Integer> getOne() {
            return new ArrayList<>(List.of(1));
        }
    }
}
