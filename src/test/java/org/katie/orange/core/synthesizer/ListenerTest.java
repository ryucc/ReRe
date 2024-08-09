package org.katie.orange.core.synthesizer;

import org.junit.jupiter.api.Test;
import org.katie.orange.core.data.objects.Node;
import org.katie.orange.core.listener.Listener;

import static org.assertj.core.api.Assertions.assertThat;


public class ListenerTest {
    @Test
    public void test() {
        Listener listener = new Listener();
        MyObjectCreatorFactory wrapped = listener.wrap(new MyObjectCreatorFactory());
        MyObjectCreator creator = wrapped.build();
        MyObject myObject = creator.create();

        int id = myObject.getId();
        String value = myObject.getValue();

        Node root = listener.getRoot();
        assertThat(root.isTerminal()).isFalse();
        assertThat(root.getRuntimeClass()).isEqualTo(MyObjectCreatorFactory.class);
        assertThat(root.getMethodCalls()).hasSize(1);

        Node creatorNode = root.getMethodCalls().getFirst().getDest();
        assertThat(creatorNode.isTerminal()).isFalse();
        assertThat(creatorNode.getRuntimeClass()).isEqualTo(MyObjectCreator.class);
        assertThat(creatorNode.getMethodCalls()).hasSize(1);

        Node objectNode = creatorNode.getMethodCalls().getFirst().getDest();
        assertThat(objectNode.isTerminal()).isFalse();
        assertThat(objectNode.getRuntimeClass()).isEqualTo(MyObject.class);
        assertThat(objectNode.getMethodCalls()).hasSize(2);
    }

    public class MyObject {
        private final int id;
        private final String value;

        public MyObject(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

    }

   public class MyObjectCreator {
        public MyObject create() {
            return new MyObject(10, "test");
        }
    }

    public class MyObjectCreatorFactory {
        public MyObjectCreator build() {
            return new MyObjectCreator();
        }
    }
}
