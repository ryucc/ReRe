package org.ingko.examples;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

import java.util.ArrayList;
import java.util.List;

public class SortExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1));
        BubbleSorter bubbleSorter = new BubbleSorter();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        BubbleSorter wrapped = environmentObjectListener.createRoot(bubbleSorter, bubbleSorter.getClass());
        wrapped.sort(arr);
        for (int i = 0; i < 2; i++) {
            System.out.println(arr.get(i));
        }
        wrapped.sort(arr);
        EnvironmentNode node = environmentObjectListener.getRoot();
        new ParameterModSynthesizer("pack", "method").generateMockito(node);
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
