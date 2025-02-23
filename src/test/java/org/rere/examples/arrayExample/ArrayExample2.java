/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.arrayExample;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.Random;

public class ArrayExample2 {
    public static void main(String[] args) {

        Dice[] twoDice = {new Dice(), new Dice()};
        DiceRoller diceRoller = new DiceRoller();

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        DiceRoller wrappedRoller = rere.createSpiedObject(diceRoller, DiceRoller.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedRoller.roll(twoDice));
        }
        System.out.println("*/");

        String code = rere.exportMockito("org.rere.examples.arrayExample", "create", "ArrayExampleExpected2");
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

    public static class DiceRoller {
        public DiceRoller(){}
        public int roll(Dice[] twoDice) {
            return twoDice[0].roll();
        }
    }

}
