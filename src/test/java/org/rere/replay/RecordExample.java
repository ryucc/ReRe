
package org.rere.replay;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.Random;

public class RecordExample {
    public static void main(String[] args) {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        TwoDice wrappedDice = rere.createSpiedObject(twoDice, TwoDice.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.dice1().roll());
            System.out.println("Rolled " + wrappedDice.dice2().roll());
        }
        System.out.println("*/");


        TwoDice replayDice = rere.createReplayMock(rere.getReReRecordData().roots().get(0), TwoDice.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + replayDice.dice1().roll());
            System.out.println("Rolled " + replayDice.dice2().roll());
        }
        System.out.println("*/");

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
