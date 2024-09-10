package org.ingko.core.listener;


import org.ingko.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;
import org.ingko.testUtils.SimpleTreePrinter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClassArrayTests {

    @Test
    public void test() throws Exception {

        Integer[] arr = {1, 2, 3, 4, 5};
        Listener listener = new Listener();

        listener.createRoot(arr, arr.getClass());

        EnvironmentNode root = listener.getRoot();
        assertThat(root);
    }

    @Test
    public void test2D() throws Exception {

        Integer[][] arr = {{1, 2}, {1, 2}};
        Listener listener = new Listener();

        listener.createRoot(arr, arr.getClass());

        EnvironmentNode root = listener.getRoot();
        assertThat(root);
    }

    @Test
    public void testLoop() throws Exception {

        Object[] arr = new Object[1];
        ArrayHolder holder = new ArrayHolder(arr);
        arr[0] = holder;

        Listener listener = new Listener();
        listener.createRoot(arr, arr.getClass());
        listener.createRoot(holder, holder.getClass());
        EnvironmentNode root1 = listener.getRoot();
        new SimpleTreePrinter().printTree(root1);
    }

    record ArrayHolder(Object[] arr) {}

    public static class Dice {
        public Dice() {
        }

        public int roll() {
            return 0;
        }
    }
}