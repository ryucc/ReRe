/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testData;

import org.rere.api.ReRe;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;
import org.rere.core.serde.DefaultSerde;

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


        ReRe rere = new ReRe();
        PrimitiveGenerator wrapped = rere.createSpiedObject(identity, PrimitiveGenerator.class);

        wrapped.getOne();
        wrapped.getTwo();
        wrapped.getString();

        EnvironmentNode node = rere.getReReRecordData().roots().get(0);
        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(getExpectedNode(), node)).isTrue();
    }

    public EnvironmentNode getExpectedNode() {
        try {
            EnvironmentNode node = EnvironmentNode.ofInternal(PrimitiveGenerator.class, PrimitiveGenerator.class);
            /* getOne */
            EnvironmentMethodCall methodCall1 = new EnvironmentMethodCall(PrimitiveGenerator.class.getMethod("getOne"));
            methodCall1.setReturnNode(EnvironmentNode.ofPrimitive(Integer.class, "1"));
            methodCall1.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall1);

            /* getOne */
            EnvironmentMethodCall methodCall2 = new EnvironmentMethodCall(PrimitiveGenerator.class.getMethod("getTwo"));
            methodCall2.setReturnNode(EnvironmentNode.ofPrimitive(Integer.class, "2"));
            methodCall2.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall2);

            /* getString */
            EnvironmentMethodCall methodCall3 = new EnvironmentMethodCall(PrimitiveGenerator.class.getMethod("getString"));
            methodCall3.setReturnNode(EnvironmentNode.ofPrimitive(String.class, new DefaultSerde().serialize("Hello, World!")));
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
