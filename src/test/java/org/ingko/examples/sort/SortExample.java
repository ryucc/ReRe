package org.ingko.examples.sort;

import org.ingko.api.Parrot;

import java.util.ArrayList;
import java.util.List;

public class SortExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1, 2, 4));
        BubbleSorter bubbleSorter = new BubbleSorter();

        Parrot parrot = Parrot.newSession();
        BubbleSorter wrapped = parrot.createRoot(bubbleSorter, bubbleSorter.getClass());
        wrapped.sort(arr);
        System.out.println("/*");
        for (int i = 0; i < 4; i++) {
            System.out.println(arr.get(i));
        }
        System.out.println("*/");
        System.out.println(parrot.createMockito("org.ingko.examples.sortExample", "method", "SortExampleExpected"));
    }

    public static class BubbleSorter {
        public BubbleSorter() {
        }

        public void sort(List<Integer> arr) {
            int n = arr.size();
            for (int i = 0; i < n; i++) {
                for (int j = 1; j < n; j++) {
                    int temp = arr.get(j);
                    int temp2 = arr.get(j - 1);
                    if (temp < temp2) {
                        arr.set(j, temp2);
                        arr.set(j - 1, temp);
                    }
                }
            }
        }
    }
}
