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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.PrimitiveSerde;

public class ArrayExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static ArrayExample.Dice environmentNode1() throws Exception {
    ArrayExample.Dice mockObject = mock(ArrayExample.Dice.class);
    int local0 = 1;
    int local1 = 5;
    int local2 = 2;
    int local3 = 6;
    int local4 = 6;
    doReturn(local0).doReturn(local1).doReturn(local2).doReturn(local3).doReturn(local4).when(mockObject).roll();
    return mockObject;
  }

  public static ArrayExample.Dice environmentNode2() throws Exception {
    ArrayExample.Dice mockObject = mock(ArrayExample.Dice.class);
    int local0 = 1;
    int local1 = 5;
    int local2 = 2;
    int local3 = 6;
    int local4 = 6;
    doReturn(local0).doReturn(local1).doReturn(local2).doReturn(local3).doReturn(local4).when(mockObject).roll();
    return mockObject;
  }

  public static ArrayExample.Dice[] environmentNode0() throws Exception {
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

  public static ArrayExample.Dice[] create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

