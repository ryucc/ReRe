package org.katie.orange.core.mockito;

import org.junit.jupiter.api.Test;
import org.katie.orange.examples.Dice;
import org.mockito.Mockito;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;


public class MockitoPlayground {
    @Test
    public void test() {
        Dice dice = Mockito.mock(Dice.class);
        doNothing().doThrow().when(dice).something();
    }
}
