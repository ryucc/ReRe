/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.arrayExample;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.Random;

public class ArrayExample {
    public static void main(String[] args) {

        Dice[] twoDice = {new Dice(), new Dice()};

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        Dice[] wrappedDice = rere.createSpiedObject(twoDice, twoDice.getClass());

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice[0].roll());
            System.out.println("Rolled " + wrappedDice[1].roll());
        }
        System.out.println("*/");
        String code = rere.exportMockito("org.rere.examples.arrayExample", "create", "ArrayExampleExpected");
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

}
