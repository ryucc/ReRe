/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.optionalExample;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.Optional;
import java.util.Random;

public class OptionalExample {
    public static void main(String[] args) {

        Optional<Dice> optionalDice = Optional.of(new Dice());

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        Optional<Dice> wrappedDice = rere.createReReObject(optionalDice, optionalDice.getClass());

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.get().roll());
        }
        System.out.println("*/");
        String code = rere.exportMockito("org.rere.examples.optionalExample", "create", "OptionalExampleExpected");
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
