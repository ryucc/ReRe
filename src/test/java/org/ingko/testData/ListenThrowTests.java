package org.ingko.testData;

import org.ingko.api.Parrot;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
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

        Parrot parrot = Parrot.newSession();
        ErrorDice wrappedDice = parrot.createRoot(dice, ErrorDice.class);


        for (int i = 1; i <= 2; i++) {
            try {
                System.out.println("Rolled " + wrappedDice.roll());
            } catch (Exception e) {
                System.out.println("Caught Exception");
            }
        }

        EnvironmentNode node = parrot.getParrotIntermediateData().roots().getFirst();


        EnvironmentNode expectedNode = getExpectedNode();
        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(node, expectedNode)).isTrue();
    }
    public EnvironmentNode getExpectedNode() throws Exception{
        EnvironmentNode expectedRoot = EnvironmentNode.ofInternal(ErrorDice.class);
        EnvironmentNode returnEnvironmentNode = EnvironmentNode.ofPrimitive(Integer.class, "2");
        EnvironmentNode throwEnvironmentNode = EnvironmentNode.ofSerialized(RuntimeException.class, "dummySerialization");
        EnvironmentMethodCall call1 = new EnvironmentMethodCall(ErrorDice.class.getMethod("roll"));
        call1.setReturnNode(returnEnvironmentNode);
        call1.setResult(MethodResult.RETURN);
        EnvironmentMethodCall call2 = new EnvironmentMethodCall(ErrorDice.class.getMethod("roll"));
        call2.setReturnNode(throwEnvironmentNode);
        call2.setResult(MethodResult.THROW);
        expectedRoot.addMethodCall(call1);
        expectedRoot.addMethodCall(call2);
        return expectedRoot;
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
