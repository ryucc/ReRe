package org.rere.examples.identityFunction;

import org.rere.api.ReRe;

import java.util.ArrayList;
import java.util.List;

public class IdentityFunctionExample {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(List.of(3, 1, 2));
        IdentityFunction identityFunction = new IdentityFunction();

        ReRe rere = ReRe.newSession();
        IdentityFunction wrapped = rere.createRoot(identityFunction, identityFunction.getClass());
        wrapped.call(arr);
        String code = rere.createMockito("org.rere.examples.identityFunction",
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
