/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testData;

import org.rere.api.ReRe;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Identity function.
 * <p>
 * Used to test if a EnvironmentMethod call can return the input parameters.
 */
public class IdentityFunction {

    @Test
    public void testGraphBuild() {
        Identity identity = new Identity();


        ReRe rere = new ReRe();
        Identity wrapped = rere.createReReObject(identity, Identity.class);

        wrapped.identityFunction(identity);

        EnvironmentNode node = rere.getReReData().getReReplayData().roots().getFirst();
        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(getExpectedNode(Identity.class), node)).isTrue();
    }

    public EnvironmentNode getExpectedNode(Class<?> expectedClass) {
        try {
            EnvironmentNode node = EnvironmentNode.ofInternal(Identity.class, Identity.class);
            EnvironmentMethodCall methodCall = new EnvironmentMethodCall(Identity.class.getMethod("identityFunction",
                    Object.class));
            methodCall.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.PARAMETER, 0));
            methodCall.setReturnNode(EnvironmentNode.ofInternal(expectedClass, expectedClass));
            methodCall.setResult(MethodResult.RETURN);
            node.addMethodCall(methodCall);
            return node;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    public class Identity {
        public Object identityFunction(Object o) {
            return o;
        }
    }
}
