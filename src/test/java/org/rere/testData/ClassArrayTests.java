/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testData;


import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ClassArrayTests {

    @Test
    public void test() throws Exception {

        Integer[] arr = {1, 2, 3, 4, 5};
        ReRe rere = ReRe.newSession();
        rere.createRoot(arr, arr.getClass());

        EnvironmentNode root = rere.getReReIntermediateData().roots().get(0);
        assertThat(root.getDirectChildren()).hasSize(5);

    }

    @Test
    public void test2D() throws Exception {

        Integer[][] arr = {{1, 2}, {1, 2}};
        ReRe rere = ReRe.newSession();

        rere.createRoot(arr, arr.getClass());

        EnvironmentNode root = rere.getReReIntermediateData().roots().get(0);
        assertThat(root.getDirectChildren()).hasSize(2);
    }

    @Test
    public void testLoop() throws Exception {

        Object[] arr = new Object[1];
        ArrayHolder holder = new ArrayHolder(arr);
        arr[0] = holder;

        ReRe rere = ReRe.newSession();
        rere.createRoot(arr, arr.getClass());
        rere.createRoot(holder, holder.getClass());
        EnvironmentNode arrRoot = rere.getReReIntermediateData().roots().get(0);
        assertThat(arrRoot.getDirectChildren()).hasSize(1);
        EnvironmentNode recordRoot = rere.getReReIntermediateData().roots().getLast();
        // TODO minor: global node map?
        // The same object has 2 nodes because of different roots
        assertThat(arrRoot == arrRoot.getDirectChildren().get(0).getDirectChildren().get(0)).isTrue();
        assertThat(recordRoot.getDirectChildren().get(0).getDirectChildren().get(0) == recordRoot).isTrue();
    }

    record ArrayHolder(Object[] arr) {
    }
}