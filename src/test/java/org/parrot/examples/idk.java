package org.parrot.examples;

public class idk {
    public static void main(String[] args) {
        B b = new B(20, 11);
        System.out.println(b.a);
        System.out.println(((A)b).a);
        b.a = 10;
        System.out.println(b.a);

    }

    public static class A {
        public final int a;
        public final int b;
        public A(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    public static class B extends A {
        public int a;
        public B(int a, int b) {
            super(a, b);
            this.a = a+1;
        }
    }
}
