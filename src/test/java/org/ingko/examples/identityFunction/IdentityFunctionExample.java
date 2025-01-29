package org.ingko.examples.identityFunction;

import org.ingko.api.Parrot;

import java.util.ArrayList;
import java.util.List;

public class IdentityFunctionExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1, 2));
        IdentityFunction identityFunction = new IdentityFunction();

        Parrot parrot = Parrot.newSession();
        IdentityFunction wrapped = parrot.createRoot(identityFunction, identityFunction.getClass());
        wrapped.call(arr);
        String code = parrot.createMockito("org.ingko.examples.identityFunction",
                "create",
                "IdentityFunctionExampleExpected");
        System.out.println(code);

    }

    public static class IdentityFunction {
        public <T> T call(T o) {
            return o;
        }
    }
}
