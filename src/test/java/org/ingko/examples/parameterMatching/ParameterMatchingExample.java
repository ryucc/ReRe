package org.ingko.examples.parameterMatching;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

public class ParameterMatchingExample {

    public static class PrivateDice {
        public PrivateDice() {
        }
        public int roll(int i, double d, long l, short s, char c, byte b, boolean bl, float f, String str) {
            return 10;
        }

    }

    public static void main(String[] args) {

        PrivateDice dice = new PrivateDice();
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        PrivateDice wrappedDice = environmentObjectListener.createRoot(dice, PrivateDice.class);


        System.out.println("/*");
        System.out.println("Rolled " + wrappedDice.roll(1, 1.0, 1L, (short) 1, 'a', (byte)0, true, 1, "abc"));
        System.out.println("*/");

        ParameterModSynthesizer mockitoSynthesizer = new ParameterModSynthesizer(
                "org.ingko.examples.parameterMatching",
                "create",
                "ParameterMatchingExampleExpected");
        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot()));
    }
}
