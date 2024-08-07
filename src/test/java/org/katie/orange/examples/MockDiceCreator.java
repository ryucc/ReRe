package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import java.lang.Integer;
import org.katie.orange.examples.Dice;
import org.mockito.Mockito;

public class MockDiceCreator {
    public static Dice create() {
        Integer mockInteger_aa5b = 2;
        Integer mockInteger_706e = 4;
        Integer mockInteger_d9b5 = 6;
        Integer mockInteger_9dd0 = 3;
        Integer mockInteger_c5dd = 4;
        Integer mockInteger_3270 = 6;
        Dice.DiceDice mockDiceDice_366c = Mockito.mock(Dice.DiceDice.class);
        doReturn(mockInteger_3270).when(mockDiceDice_366c).roll();
        Integer mockInteger_33db = 1;
        Dice.DiceDice mockDiceDice_caad = Mockito.mock(Dice.DiceDice.class);
        doReturn(mockInteger_33db).when(mockDiceDice_caad).roll();
        Integer mockInteger_fdc5 = 2;
        Dice.DiceDice mockDiceDice_3f07 = Mockito.mock(Dice.DiceDice.class);
        doReturn(mockInteger_fdc5).when(mockDiceDice_3f07).roll();
        Integer mockInteger_083a = 1;
        Dice.DiceDice mockDiceDice_488f = Mockito.mock(Dice.DiceDice.class);
        doReturn(mockInteger_083a).when(mockDiceDice_488f).roll();
        Integer mockInteger_9b11 = 5;
        Dice.DiceDice mockDiceDice_d274 = Mockito.mock(Dice.DiceDice.class);
        doReturn(mockInteger_9b11).when(mockDiceDice_d274).roll();
        Dice mockDice_a6fd = Mockito.mock(Dice.class);
        doReturn(mockInteger_aa5b).doReturn(mockInteger_706e).doReturn(mockInteger_d9b5).doReturn(mockInteger_9dd0).doReturn(mockInteger_c5dd).when(mockDice_a6fd).roll();
        doReturn(mockDiceDice_366c).doReturn(mockDiceDice_caad).doReturn(mockDiceDice_3f07).doReturn(mockDiceDice_488f).doReturn(mockDiceDice_d274).when(mockDice_a6fd).getDiceDice();
        return mockDice_a6fd;
    }
}