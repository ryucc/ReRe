
package org.rere.replay;

import org.rere.api.ReRe;
import org.rere.api.ReReData;
import org.rere.api.ReReMode;
import org.rere.api.ReReSettings;
import org.rere.api.ReReplayData;

import java.util.Random;

public class RecordExample {
    public static void main(String[] args) {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());

        ReRe rere = new ReRe(new ReReSettings().withParameterModding(true));
        TwoDice wrappedDice = rere.createReReObject(twoDice, TwoDice.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.dice1().roll());
            System.out.println("Rolled " + wrappedDice.dice2().roll());
        }
        System.out.println("*/");

        ReReData reReplayData = rere.getReReData();
        ReRe replayReRe = new ReRe(new ReReSettings().withReReMode(ReReMode.REPLAY)
                .withReReplayData(reReplayData));


        TwoDice replayDice = replayReRe.createReReObject(null, TwoDice.class);

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
