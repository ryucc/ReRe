Rolled 10
package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import org.mockito.Mockito;
import org.parrot.core.serde.DefaultSerde;
import org.parrot.examples.ParameterExample;

public class MockPrivateDiceCreator {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ParameterExample.PrivateDice create() {
    ParameterExample.PrivateDice mockPrivateDice1 = Mockito.mock(ParameterExample.PrivateDice.class);
    doReturn(10).when(mockPrivateDice1).roll(anyInt(), anyDouble(), anyLong(), anyShort(), anyChar(), anyByte(), anyBoolean(), anyFloat(), anyString());
    return mockPrivateDice1;
  }
}

