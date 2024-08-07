package com.example.helloworld;

import static org.mockito.Mockito.doReturn;

import org.katie.orange.examples.Dice;
import org.mockito.Mockito;

public class MockDiceCreator {
    public static Dice create() {
        Dice.DiceDice mockDiceDice_2487 = Mockito.mock(Dice.DiceDice.class);
        doReturn(4).when(mockDiceDice_2487).roll();
        Dice.DiceDice mockDiceDice_8c22 = Mockito.mock(Dice.DiceDice.class);
        doReturn(2).when(mockDiceDice_8c22).roll();
        Dice.DiceDice mockDiceDice_1d4f = Mockito.mock(Dice.DiceDice.class);
        doReturn(5).when(mockDiceDice_1d4f).roll();
        Dice.DiceDice mockDiceDice_be45 = Mockito.mock(Dice.DiceDice.class);
        doReturn(6).when(mockDiceDice_be45).roll();
        Dice.DiceDice mockDiceDice_d1b3 = Mockito.mock(Dice.DiceDice.class);
        doReturn(3).when(mockDiceDice_d1b3).roll();
        Dice mockDice_a2cc = Mockito.mock(Dice.class);
        doReturn(4).doReturn(3).doReturn(5).doReturn(5).doReturn(6).when(mockDice_a2cc).roll();
        doReturn(mockDiceDice_2487).doReturn(mockDiceDice_8c22).doReturn(mockDiceDice_1d4f).doReturn(mockDiceDice_be45).doReturn(mockDiceDice_d1b3).when(mockDice_a2cc).getDiceDice();
        return mockDice_a2cc;
    }
}