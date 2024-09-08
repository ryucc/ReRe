package org.ingko.core.listener;

import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.MethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.Node;

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
        Node root = listener.getRoot();
        Node expectedRoot = Node.ofInternal(ErrorDice.class);
        Node returnNode = Node.ofPrimitive(int.class, "2");
        Node throwNode = Node.ofSerialized(RuntimeException.class, "dummySerialization");
        MethodCall call1 = new MethodCall(ErrorDice.class.getMethod("roll"),
                expectedRoot,
                returnNode,
                MethodResult.RETURN
                );
        MethodCall call2 = new MethodCall(ErrorDice.class.getMethod("roll"),
                expectedRoot,
                throwNode,
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
