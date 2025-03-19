/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.sort;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.ArrayList;
import java.util.List;

public class SortExample {

    public static void main(String[] args) {
        List<MyInt> arr = new ArrayList<>(List.of(new MyInt(3), new MyInt(2)));
        BubbleSorter bubbleSorter = new BubbleSorter();

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        BubbleSorter wrapped = rere.createReReObject(bubbleSorter, bubbleSorter.getClass());
        wrapped.sort(arr);
        System.out.println("/*");
        for (int i = 0; i < 2; i++) {
            System.out.println(arr.get(i).value());
        }
        System.out.println("*/");
        System.out.println(rere.exportMockito("org.rere.examples.sort", "method", "SortExampleExpected"));
    }

    public static class BubbleSorter {
        public BubbleSorter() {
        }

        public void sort(List<MyInt> arr) {
            int n = arr.size();
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < n; j++) {
                    MyInt temp = arr.get(j);
                    MyInt temp2 = arr.get(j - 1);
                    if (temp.compare(temp2)) {
                        arr.set(j, temp2);
                        arr.set(j - 1, temp);
                    }
                }
            }
        }
    }

    public static class MyInt {
        final int value;

        public MyInt(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public boolean compare(MyInt other) {
            return other.value() > value();
        }

    }
}
