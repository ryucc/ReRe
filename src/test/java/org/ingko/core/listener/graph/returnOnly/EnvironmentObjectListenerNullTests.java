package org.ingko.core.listener.graph.returnOnly;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentObjectListenerNullTests {
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
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        NullDice wrappedDice = environmentObjectListener.createRoot(dice, NullDice.class);

        wrappedDice.rollNull();

        EnvironmentNode root = environmentObjectListener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(NullDice.class);
        assertThat(root.getMethodCalls()).hasSize(1)
                .extracting(EnvironmentMethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(EnvironmentMethodCall::getDest)
                .map(EnvironmentNode::getValue)
                .containsExactly("null");
    }
    @Test
    public void testVoid() throws Exception {

        NullDice dice = new NullDice();
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        NullDice wrappedDice = environmentObjectListener.createRoot(dice, NullDice.class);

        wrappedDice.rollVoid();

        EnvironmentNode root = environmentObjectListener.getRoot();
        assertThat(root.getRuntimeClass()).isEqualTo(NullDice.class);
        assertThat(root.getMethodCalls()).hasSize(1)
                .extracting(EnvironmentMethodCall::getResult)
                .allMatch(result -> result == MethodResult.RETURN);
        assertThat(root.getMethodCalls())
                .extracting(EnvironmentMethodCall::getDest)
                .map(EnvironmentNode::getRuntimeClass)
                .first()
                .isEqualTo(void.class);
    }
}