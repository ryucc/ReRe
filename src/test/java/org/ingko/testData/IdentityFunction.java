package org.ingko.testData;

import org.ingko.api.Parrot;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
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


        Parrot parrot = Parrot.newSession();
        Identity wrapped = parrot.createRoot(identity, Identity.class);

        wrapped.identityFunction(identity);

        EnvironmentNode node = parrot.getParrotIntermediateData().roots().getFirst();
        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(getExpectedNode(Identity.class), node)).isTrue();
    }

    public EnvironmentNode getExpectedNode(Class<?> expectedClass) {
        try {
            EnvironmentNode node = EnvironmentNode.ofInternal(Identity.class);
            EnvironmentMethodCall methodCall = new EnvironmentMethodCall(Identity.class.getMethod("identityFunction",
                    Object.class));
            methodCall.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.PARAMETER, 0));
            methodCall.setReturnNode(EnvironmentNode.ofInternal(expectedClass));
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
