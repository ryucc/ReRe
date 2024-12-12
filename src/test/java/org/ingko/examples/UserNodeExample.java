package org.ingko.examples;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

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
        new ParameterModSynthesizer("pack", "method").generateMockito(node);
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
