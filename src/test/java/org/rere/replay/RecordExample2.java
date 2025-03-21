/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.replay;

import org.junit.jupiter.api.Test;
import org.rere.api.ReRe;
import org.rere.api.ReReData;
import org.rere.api.ReReMode;
import org.rere.api.ReReSettings;
import org.rere.api.ReReplayData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordExample2 {

    @Test
    public void test() {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());
        DiceRoller diceRoller = new DiceRoller();

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        DiceRoller wrappedRoller = rere.createReReObject(diceRoller, DiceRoller.class);

        List<Integer> values = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            values.add(wrappedRoller.roll(twoDice).getValue());
        }

        ReReData reReplayData = rere.getReReData();

        ReRe replayReRe = new ReRe(new ReReSettings().withReReMode(ReReMode.REPLAY)
                .withReReplayData(reReplayData));

        DiceRoller replayRoller = rere.createReReObject(diceRoller, DiceRoller.class);

        List<Integer> replayValues = new ArrayList<>();

        TwoDice secondDice = new TwoDice(new Dice(), new Dice());
        for (int i = 1; i <= 5; i++) {
            replayValues.add(replayRoller.roll(secondDice).getValue());
        }
        assertThat(replayValues).usingRecursiveComparison().isEqualTo(values);
    }

    public static class Dice {

        private final Random rand;

        public Dice() {
            rand = new Random(0);
        }

        public MyInt roll() {
            return new MyInt(rand.nextInt(6) + 1);
        }

    }

    public record TwoDice(Dice dice1, Dice dice2) {
    }

    public static class DiceRoller {
        public DiceRoller() {
        }

        public MyInt roll(TwoDice twoDice) {
            return twoDice.dice1().roll();
        }
    }

    public static class MyInt {
        public int getValue() {
            return value;
        }

        final int value;

        public MyInt(int value) {
            this.value = value;
        }
    }

}
