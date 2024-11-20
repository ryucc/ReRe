package org.ingko.core.listener;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class MockitoTest {


    public static class Dice {

        private final Random rand;

        public Dice() {
            rand = new Random(0);
        }

        public int roll(int i) {
            return rand.nextInt(6) + 1;
        }

    }

    public Answer someAnswer() {
        return (InvocationOnMock i) -> null;
    }

    @Test
    public void test() {
        Dice myDice = mock(Dice.class);

        doAnswer(invocation -> {
            Object index = invocation.getArgument(0);

            // verify the invocation is called with the index
            assertEquals(1, index);

            // return the value we want
            return 1000;
        }).when(myDice).roll(any(Integer.class));

        assertEquals(1000, myDice.roll(1));
    }

}
