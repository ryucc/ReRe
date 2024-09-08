package org.ingko.core.listener;

import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.MethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.Node;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerPrivateTests {
    private static class PrivateDice {

        private final Random rand;
        public PrivateDice() {
            rand = new Random(0);
        }
        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    @Test
    public void test() {

        PrivateDice dice = new PrivateDice();
        Listener listener = new Listener();
        PrivateDice wrappedDice = listener.createRoot(dice, PrivateDice.class);

        for (int i = 1; i <= 5; i++) {
            wrappedDice.roll();
        }

        Node root = listener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(PrivateDice.class);
        assertThat(root.getMethodCalls()).hasSize(5)
                .extracting(MethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(MethodCall::getDest)
                .map(Node::getValue)
                .containsExactly("1", "5", "2", "6", "6");
    }
}