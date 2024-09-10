package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;

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

        EnvironmentNode root = listener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(PrivateDice.class);
        assertThat(root.getMethodCalls()).hasSize(5)
                .extracting(EnvironmentMethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(EnvironmentMethodCall::getDest)
                .map(EnvironmentNode::getValue)
                .containsExactly("1", "5", "2", "6", "6");
    }
}