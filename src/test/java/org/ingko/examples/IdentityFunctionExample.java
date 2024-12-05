package org.ingko.examples;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;

import java.util.ArrayList;
import java.util.List;

public class IdentityFunctionExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1, 2));
        SortExample.BubbleSorter bubbleSorter = new SortExample.BubbleSorter();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        SortExample.BubbleSorter wrapped = environmentObjectListener.createRoot(bubbleSorter, bubbleSorter.getClass());
        wrapped.sort(arr);
        for (int i = 0; i < 3; i++) {
            System.out.println(arr.get(i));
        }
        EnvironmentNode node = environmentObjectListener.getRoot();
        return;
    }

    public static class IdentityFunction {
        public <T> T call(T o) {
            return o;
        }
    }
}
