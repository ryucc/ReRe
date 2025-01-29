package org.ingko.examples.exception;

import org.ingko.api.Parrot;

import java.util.Random;

public class ThrowExample {
    public static void main(String[] args) throws Exception {

        ErrorDice dice = new ErrorDice();

        Parrot parrot = Parrot.newSession();
        ErrorDice wrappedDice = parrot.createRoot(dice, ErrorDice.class);


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

        String code = parrot.createMockito("org.ingko.examples.exception", "create", "ThrowExampleExpected");
        System.out.println(code);
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
