/*
Rolled 4
Rolled 5
Rolled 2
Rolled 4
Rolled 3
*/
package org.ingko.examples.readme;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.ingko.core.serde.DefaultSerde;

public class ReadmeExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ReadmeExample.PrivateDice environmentNode0() {
    ReadmeExample.PrivateDice mockObject = mock(ReadmeExample.PrivateDice.class);
    doNothing().when(mockObject).chill();
    doReturn(4).doReturn(5).doReturn(2).doReturn(4).doReturn(3).when(mockObject).roll();
    return mockObject;
  }
}

