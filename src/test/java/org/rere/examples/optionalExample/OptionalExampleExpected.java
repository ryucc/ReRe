/*
Rolled 1
Rolled 5
Rolled 2
Rolled 6
Rolled 6
*/
package org.rere.examples.optionalExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.util.Optional;
import org.rere.core.serde.PrimitiveSerde;

public class OptionalExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static OptionalExample.Dice environmentNode1() throws Exception {
    OptionalExample.Dice mockObject = mock(OptionalExample.Dice.class);
    doReturn((int) 1).doReturn((int) 5).doReturn((int) 2).doReturn((int) 6).doReturn((int) 6).when(mockObject).roll();
    return mockObject;
  }

  public static Optional<OptionalExample.Dice> environmentNode0() throws Exception {
    Optional<OptionalExample.Dice> object0;
    OptionalExample.Dice object1;
    object1 = environmentNode1();
    object0 = Optional.of(object1);
    return object0;
  }

  public static Optional<OptionalExample.Dice> create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

