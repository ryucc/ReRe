package org.rere.examples.readme;

import org.rere.api.ReRe;

import java.util.Random;

public class ReadmeExample {
    public static void main(String[] args) {

        Dice dice = new Dice();
        ReRe reRe = ReRe.newSession();
        Dice wrappedDice = reRe.createRoot(dice, Dice.class);


        System.out.println("/*");
        wrappedDice.chill();
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }
        System.out.println("*/");

        String code = reRe.createMockito("org.rere.examples.readme", "create", "ReadmeExampleExpected");
        System.out.println(code);
    }

    public static class Dice {
        private final Random rand;

        public Dice() {
            rand = new Random(1);
        }

        public void chill() {
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }
}