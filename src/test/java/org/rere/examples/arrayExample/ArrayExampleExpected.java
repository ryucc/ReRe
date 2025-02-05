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
package org.rere.examples.arrayExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.rere.core.serde.DefaultSerde;

public class ArrayExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ArrayExample.Dice environmentNode1() {
    ArrayExample.Dice mockObject = mock(ArrayExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockObject).roll();
    return mockObject;
  }

  public static ArrayExample.Dice environmentNode2() {
    ArrayExample.Dice mockObject = mock(ArrayExample.Dice.class);
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockObject).roll();
    return mockObject;
  }

  public static ArrayExample.Dice[] environmentNode0() {
    ArrayExample.Dice[] object0;
    ArrayExample.Dice object1;
    ArrayExample.Dice object2;
    object0 = new ArrayExample.Dice[2];
    object1 = environmentNode1();
    object2 = environmentNode2();
    object0[0] = object1;
    object0[1] = object2;
    return object0;
  }
}

