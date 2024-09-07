Rolled 1
Rolled 1
Rolled 5
Rolled 5
Rolled 2
Rolled 2
Rolled 6
Rolled 6
Rolled 6
Rolled 6
package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import org.mockito.Mockito;
import org.parrot.core.serde.DefaultSerde;
import org.parrot.examples.RecordExample;

public class MockTwoDiceCreator {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static RecordExample.TwoDice create() {
    RecordExample.Dice mockDice1 = Mockito.mock(RecordExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockDice1).roll();
    RecordExample.Dice mockDice2 = Mockito.mock(RecordExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockDice2).roll();
    RecordExample.TwoDice mockTwoDice1 = new RecordExample.TwoDice(mockDice1, mockDice2);
    return mockTwoDice1;
  }
}

