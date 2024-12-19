package org.ingko.testData;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Identity function.
 * <p>
 * Used to test if a EnvironmentMethod call can return the input parameters.
 */
public class PrimitiveReturns {

    @Test
    public void testGraphBuild() {
        PrimitiveGenerator identity = new PrimitiveGenerator();


        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        PrimitiveGenerator wrapped = environmentObjectListener.createRoot(identity, PrimitiveGenerator.class);

        wrapped.getOne();
        wrapped.getTwo();
        wrapped.getString();

        EnvironmentNode node = environmentObjectListener.getRoot();
        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(getExpectedNode(), node)).isTrue();
    }

    public EnvironmentNode getExpectedNode() {
        try {
            EnvironmentNode node = EnvironmentNode.ofInternal(PrimitiveGenerator.class);
            /* getOne */
            EnvironmentMethodCall methodCall1 = new EnvironmentMethodCall(
                    PrimitiveGenerator.class.getMethod("getOne"));
            methodCall1.setReturnNode(EnvironmentNode.ofPrimitive(Integer.class, "1"));
            methodCall1.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall1);

            /* getOne */
            EnvironmentMethodCall methodCall2 = new EnvironmentMethodCall(
                    PrimitiveGenerator.class.getMethod("getTwo"));
            methodCall2.setReturnNode(EnvironmentNode.ofPrimitive(Integer.class, "2"));
            methodCall2.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall2);

            /* getString */
            EnvironmentMethodCall methodCall3 = new EnvironmentMethodCall(
                    PrimitiveGenerator.class.getMethod("getString"));
            methodCall3.setReturnNode(EnvironmentNode.ofPrimitive(String.class, "\"Hello, World!\""));
            methodCall3.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall3);
            return node;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    public static class PrimitiveGenerator {
        public Integer getOne() {
            return 1;
        }
        public Integer getTwo() {
            return 2;
        }
        public String getString() {
            return "Hello, World!";
        }
    }
}
