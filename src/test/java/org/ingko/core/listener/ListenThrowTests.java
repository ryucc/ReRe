package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenThrowTests {
    @Test
    public void test() throws Exception {

        ErrorDice dice = new ErrorDice();

        Listener listener = new Listener();
        ErrorDice wrappedDice = listener.createRoot(dice, ErrorDice.class);


        for (int i = 1; i <= 2; i++) {
            try {
                System.out.println("Rolled " + wrappedDice.roll());
            } catch (Exception e) {
                System.out.println("Caught Exception");
            }
        }
        EnvironmentNode root = listener.getRoot();
        EnvironmentNode expectedRoot = EnvironmentNode.ofInternal(ErrorDice.class);
        EnvironmentNode returnEnvironmentNode = EnvironmentNode.ofPrimitive(int.class, "2");
        EnvironmentNode throwEnvironmentNode = EnvironmentNode.ofSerialized(RuntimeException.class, "dummySerialization");
        EnvironmentMethodCall call1 = new EnvironmentMethodCall(ErrorDice.class.getMethod("roll"),
                expectedRoot,
                returnEnvironmentNode,
                MethodResult.RETURN
                );
        EnvironmentMethodCall call2 = new EnvironmentMethodCall(ErrorDice.class.getMethod("roll"),
                expectedRoot,
                throwEnvironmentNode,
                MethodResult.THROW
        );
        expectedRoot.addEdge(call1);
        expectedRoot.addEdge(call2);


        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(root, expectedRoot)).isTrue();
    }

    public static class ErrorDice {

        private final Random rand;
        private int count;

        public ErrorDice() {
            rand = new Random(1239);
            count = 0;
        }

        public int roll() {
            if (count == 1) {
                count = 0;
                throw new RuntimeException("dice throws on 3rd invocation.");
            }
            count++;
            return rand.nextInt(6) + 1;
        }

    }
}
