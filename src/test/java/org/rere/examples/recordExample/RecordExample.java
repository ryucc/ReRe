/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.recordExample;

import org.rere.api.ReRe;

import java.util.Random;

public class RecordExample {
    public static void main(String[] args) {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());

        ReRe rere = new ReRe();
        TwoDice wrappedDice = rere.createSpiedObject(twoDice, TwoDice.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.dice1().roll());
            System.out.println("Rolled " + wrappedDice.dice2().roll());
        }
        System.out.println("*/");

        String code = rere.exportMockito("org.rere.examples.recordExample", "create", "RecordExampleExpected");
        System.out.println(code);
    }

    public static class Dice {

        private final Random rand;

        public Dice() {
            rand = new Random(0);
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    public record TwoDice(Dice dice1, Dice dice2) {
    }
}
