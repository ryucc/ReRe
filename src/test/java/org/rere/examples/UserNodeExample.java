/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.interceptor.EnvironmentObjectListener;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.ArrayList;
import java.util.List;

public class UserNodeExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1));
        ObjectModifier objectModifier = new ObjectModifier();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        ObjectModifier wrapped = environmentObjectListener.createRoot(objectModifier, ObjectModifier.class);
        MyClass obj = new MyClass();
        MyClass[] myClasses = {obj};
        MyRecord myRecord = new MyRecord(myClasses);
        wrapped.modify(myRecord);
        EnvironmentNode node = environmentObjectListener.getRoot();
        System.out.println(new MockitoSynthesizer("pack", "method").generateMockito(node));
    }

    public static class MyClass {
        private int count;

        public MyClass() {
            this.count = 0;
        }

        public void add(int a) {
            count += a;
        }

    }
    public static class ObjectModifier {
        public ObjectModifier() {
        }

        public void modify(MyRecord myRecord) {
            myRecord.myClasses()[0].add(1);
        }
    }
    public record MyRecord(MyClass[] myClasses){}
}
