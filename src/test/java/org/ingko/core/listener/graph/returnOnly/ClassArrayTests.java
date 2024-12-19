package org.ingko.core.listener.graph.returnOnly;


import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.junit.jupiter.api.Test;
import org.ingko.testUtils.SimpleTreePrinter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ClassArrayTests {

    @Test
    public void test() throws Exception {

        Integer[] arr = {1, 2, 3, 4, 5};
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();

        environmentObjectListener.createRoot(arr, arr.getClass());

        EnvironmentNode root = environmentObjectListener.getRoot();
        assertThat(root.getDirectChildren()).hasSize(5);

    }

    @Test
    public void test2D() throws Exception {

        Integer[][] arr = {{1, 2}, {1, 2}};
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();

        environmentObjectListener.createRoot(arr, arr.getClass());

        EnvironmentNode root = environmentObjectListener.getRoot();
        assertThat(root.getDirectChildren()).hasSize(2);
    }

    @Test
    public void testLoop() throws Exception {

        Object[] arr = new Object[1];
        ArrayHolder holder = new ArrayHolder(arr);
        arr[0] = holder;

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        environmentObjectListener.createRoot(arr, arr.getClass());
        environmentObjectListener.createRoot(holder, holder.getClass());
        EnvironmentNode root1 = environmentObjectListener.getRoot();
        new SimpleTreePrinter().printTree(root1);
    }

    record ArrayHolder(Object[] arr) {}
}