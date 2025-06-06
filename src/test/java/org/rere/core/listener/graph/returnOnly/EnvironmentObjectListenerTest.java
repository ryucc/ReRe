/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.graph.returnOnly;

import org.assertj.core.api.Assertions;
import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EnvironmentObjectListenerTest {
    @Test
    public void test() {
        ReRe reRe = new ReRe();
        MyObjectCreatorFactory wrapped = reRe.createReReObject(new MyObjectCreatorFactory(), MyObjectCreatorFactory.class);
        MyObjectCreator creator = wrapped.build();
        MyObject myObject = creator.create();

        int id = myObject.getId();
        String value = myObject.getValue();

        EnvironmentNode root = reRe.getReReData().getReReplayData().roots().getFirst();
        assertThat(root.isTerminal()).isFalse();
        assertThat(root.getRuntimeClass()).isEqualTo(MyObjectCreatorFactory.class);
        Assertions.assertThat(root.getMethodCalls()).hasSize(1);

        EnvironmentNode creatorEnvironmentNode = root.getMethodCalls().getFirst().getDest();
        assertThat(creatorEnvironmentNode.isTerminal()).isFalse();
        assertThat(creatorEnvironmentNode.getRuntimeClass()).isEqualTo(MyObjectCreator.class);
        Assertions.assertThat(creatorEnvironmentNode.getMethodCalls()).hasSize(1);

        EnvironmentNode objectEnvironmentNode = creatorEnvironmentNode.getMethodCalls().getFirst().getDest();
        assertThat(objectEnvironmentNode.isTerminal()).isFalse();
        assertThat(objectEnvironmentNode.getRuntimeClass()).isEqualTo(MyObject.class);
        Assertions.assertThat(objectEnvironmentNode.getMethodCalls()).hasSize(2);
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
