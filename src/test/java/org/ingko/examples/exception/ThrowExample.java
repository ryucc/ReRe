package org.ingko.examples.exception;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.Random;

public class ThrowExample {
    public static void main(String[] args) throws Exception {

        ErrorDice dice = new ErrorDice();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        ErrorDice wrappedDice = environmentObjectListener.createRoot(dice, ErrorDice.class);


        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            try {
                System.out.println("Rolled " + wrappedDice.roll());
            } catch (Exception e) {
                System.out.println("Exception thrown.");
                e.printStackTrace();
            }
        }
        System.out.println("*/");

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.ingko.examples.exception",
                "create",
                "ThrowExampleExpected");
        String result = mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot());
        System.out.println(result);
    }

    public static class ErrorDice {

        private final Random rand;
        private int count;

        public ErrorDice() {
            rand = new Random(1239);
            count = 0;
        }

        public int roll() {
            if (count == 2) {
                count = 0;
                throw new RuntimeException("dice throws on 3rd invocation.");
            }
            count++;
            return rand.nextInt(6) + 1;
        }

    }
}
