package org.parrot.core.listener;

import org.junit.jupiter.api.Test;
import org.parrot.core.data.methods.MethodCall;
import org.parrot.core.data.methods.MethodResult;
import org.parrot.core.data.objects.Node;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerNullTests {
    private static class NullDice {
        // TODO: can't mock with out declared constructor
        NullDice(){}

        public void rollVoid() {
        }

        public Integer rollNull() {
            int a = 0;
            a++;
            if(a == 1) {
                return null;
            }
            return null;
        }

        public int roll() {
            return 1;
        }
    }

    @Test
    public void testNull() {

        NullDice dice = new NullDice();
        Listener listener = new Listener();
        NullDice wrappedDice = listener.createRoot(dice, NullDice.class);

        wrappedDice.rollNull();

        Node root = listener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(NullDice.class);
        assertThat(root.getMethodCalls()).hasSize(1)
                .extracting(MethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(MethodCall::getDest)
                .map(Node::getValue)
                .containsExactly("null");
    }
    @Test
    public void testVoid() throws Exception {

        NullDice dice = new NullDice();
        Listener listener = new Listener();
        NullDice wrappedDice = listener.createRoot(dice, NullDice.class);

        wrappedDice.rollVoid();

        Node root = listener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(NullDice.class);
        assertThat(root.getMethodCalls()).hasSize(1)
                .extracting(MethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(MethodCall::getDest)
                .map(Node::getRuntimeClass)
                .first()
                .isEqualTo(void.class);
    }
}