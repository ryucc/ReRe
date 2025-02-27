/*
Rolled 2
Rolled 2
Exception thrown.
Rolled 1
Rolled 4
*/
package org.rere.examples.exception;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.PrimitiveSerde;

public class ThrowExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static RuntimeException environmentNode1() throws Exception {
    // java.lang.RuntimeException: dice throws on 3rd invocation.
    return (RuntimeException) defaultSerde.deserialize("");
  }

  public static ThrowExample.ErrorDice environmentNode0() throws Exception {
    ThrowExample.ErrorDice mockObject = mock(ThrowExample.ErrorDice.class);
    int local0 = 2;
    int local1 = 2;
    RuntimeException local2 = environmentNode1();
    int local3 = 1;
    int local4 = 4;
    doReturn(local0).doReturn(local1).doThrow(local2).doReturn(local3).doReturn(local4).when(mockObject).roll();
    return mockObject;
  }

  public static ThrowExample.ErrorDice create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

