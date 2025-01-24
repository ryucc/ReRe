package org.ingko.examples.identityFunction;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.ArrayList;
import java.util.List;

public class IdentityFunctionExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1, 2));
        IdentityFunction identityFunction = new IdentityFunction();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        IdentityFunction wrapped = environmentObjectListener.createRoot(identityFunction, identityFunction.getClass());
        wrapped.call(arr);
        EnvironmentNode node = environmentObjectListener.getRoot();
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(
                "org.ingko.examples.identityFunction",
                "create",
                "IdentityFunctionExampleExpected");
        System.out.println(mockitoSynthesizer.generateMockito(node));

    }

    public static class IdentityFunction {
        public <T> T call(T o) {
            return o;
        }
    }
}
