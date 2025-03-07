/*
Rolled 4
Rolled 5
Rolled 2
Rolled 4
Rolled 3
*/
package org.rere.examples.readme;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.PrimitiveSerde;

public class ReadmeExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static ReadmeExample.Dice environmentNode0() throws Exception {
    ReadmeExample.Dice mockObject = mock(ReadmeExample.Dice.class);
    doNothing().when(mockObject).chill();
    int local0 = 4;
    int local1 = 5;
    int local2 = 2;
    int local3 = 4;
    int local4 = 3;
    doReturn(local0).doReturn(local1).doReturn(local2).doReturn(local3).doReturn(local4).when(mockObject).roll();
    return mockObject;
  }

  public static ReadmeExample.Dice create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

