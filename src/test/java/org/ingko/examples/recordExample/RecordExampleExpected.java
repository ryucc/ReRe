/*
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
*/
package org.ingko.examples.recordExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.ingko.core.serde.DefaultSerde;

public class RecordExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static RecordExample.Dice environmentNode1() {
    RecordExample.Dice mockObject = mock(RecordExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockObject).roll();
    return mockObject;
  }

  public static RecordExample.Dice environmentNode2() {
    RecordExample.Dice mockObject = mock(RecordExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockObject).roll();
    return mockObject;
  }

  public static RecordExample.TwoDice environmentNode0() {
    RecordExample.TwoDice object0;
    RecordExample.Dice object1;
    RecordExample.Dice object2;
    object1 = environmentNode1();
    object2 = environmentNode2();
    object0 = new RecordExample.TwoDice(object1,object2);
    return object0;
  }
}

