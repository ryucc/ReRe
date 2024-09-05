package org.parrot.examples;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;

import java.util.Random;

public class ThrowExample {
    public static void main(String[] args) throws Exception {

        ErrorDice dice = new ErrorDice();

        Listener listener = new Listener();
        ErrorDice wrappedDice = listener.createRoot(dice, ErrorDice.class);


        for (int i = 1; i <= 5; i++) {
            try {
                System.out.println("Rolled " + wrappedDice.roll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.parrot.examples", "create");
        String result = mockitoSynthesizer.generateMockito(listener);
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
