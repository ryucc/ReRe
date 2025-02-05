package org.rere.examples.parameterMatching;

import org.rere.api.ReRe;

public class ParameterMatchingExample {

    public static void main(String[] args) {

        PrivateDice dice = new PrivateDice();
        ReRe rere = ReRe.newSession();
        PrivateDice wrappedDice = rere.createRoot(dice, PrivateDice.class);


        System.out.println("/*");
        System.out.println("Rolled " + wrappedDice.roll(1, 1.0, 1L, (short) 1, 'a', (byte) 0, true, 1, "abc"));
        System.out.println("*/");

        String code = rere.createMockito("org.rere.examples.parameterMatching",
                "create",
                "ParameterMatchingExampleExpected");
        System.out.println(code);
    }

    public static class PrivateDice {
        public PrivateDice() {
        }

        public int roll(int i, double d, long l, short s, char c, byte b, boolean bl, float f, String str) {
            return 10;
        }

    }
}
