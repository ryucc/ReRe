/*
Rolled 10
*/
package org.rere.examples.parameterMatching;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.DefaultSerde;

public class ParameterMatchingExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ParameterMatchingExample.PrivateDice environmentNode0() throws Exception {
    ParameterMatchingExample.PrivateDice mockObject = mock(ParameterMatchingExample.PrivateDice.class);
    doReturn((int) 10).when(mockObject).roll(anyInt(), anyDouble(), anyLong(), anyShort(), anyChar(), anyByte(), anyBoolean(), anyFloat(), anyString());
    return mockObject;
  }

  public static ParameterMatchingExample.PrivateDice create() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

