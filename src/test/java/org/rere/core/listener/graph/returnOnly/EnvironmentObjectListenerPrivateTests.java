/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.graph.returnOnly;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.interceptor.EnvironmentObjectListener;
import org.junit.jupiter.api.Test;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

//TODO: fix private class mocking
public class EnvironmentObjectListenerPrivateTests {

    public static class PrivateDice {

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
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        PrivateDice wrappedDice = environmentObjectListener.createRoot(dice, PrivateDice.class);

        for (int i = 1; i <= 5; i++) {
            wrappedDice.roll();
        }

        EnvironmentNode root = environmentObjectListener.getRoot();
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