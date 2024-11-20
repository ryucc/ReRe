/*
Rolled 4
Rolled 5
Rolled 2
Rolled 4
Rolled 3
*/
package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import org.ingko.core.serde.DefaultSerde;
import org.ingko.examples.ReadmeExample;
import org.mockito.Mockito;

public class MockCreator {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ReadmeExample.PrivateDice create() {
    ReadmeExample.PrivateDice mockPrivateDice1 = Mockito.mock(ReadmeExample.PrivateDice.class);
    doReturn(4).doReturn(5).doReturn(2).doReturn(4).doReturn(3).when(mockPrivateDice1).roll();
    return mockPrivateDice1;
  }
}

