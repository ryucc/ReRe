package org.ingko.core.listener.graph.returnOnly;


import org.ingko.api.Parrot;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.junit.jupiter.api.Test;

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

        Parrot parrot = Parrot.newSession();
        parrot.createRoot(arr, arr.getClass());
        parrot.createRoot(holder, holder.getClass());
        EnvironmentNode arrRoot = parrot.getParrotIntermediateData().roots().getFirst();
        assertThat(arrRoot.getDirectChildren()).hasSize(1);
        EnvironmentNode recordRoot = parrot.getParrotIntermediateData().roots().getLast();
        // TODO minor: global node map?
        // The same object has 2 nodes because of different roots
        assertThat(arrRoot == arrRoot.getDirectChildren().getFirst().getDirectChildren().getFirst()).isTrue();
        assertThat(recordRoot.getDirectChildren().getFirst().getDirectChildren().getFirst() == recordRoot).isTrue();
    }

    record ArrayHolder(Object[] arr) {
    }
}