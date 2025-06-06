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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.RuntimeException;
import java.util.Optional;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.PrimitiveSerde;

public class OptionalExampleExpected2 {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static Answer<Integer> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      Optional<OptionalExample2.Dice> param0 = invocation.getArgument(0);
      param0.get().roll();
      return 1;
    } ;
  }

  public static Answer<Integer> getAnswer1() {
    return (InvocationOnMock invocation) -> {
      Optional<OptionalExample2.Dice> param0 = invocation.getArgument(0);
      param0.get().roll();
      return 5;
    } ;
  }

  public static Answer<Integer> getAnswer2() {
    return (InvocationOnMock invocation) -> {
      Optional<OptionalExample2.Dice> param0 = invocation.getArgument(0);
      param0.get().roll();
      return 2;
    } ;
  }

  public static Answer<Integer> getAnswer3() {
    return (InvocationOnMock invocation) -> {
      Optional<OptionalExample2.Dice> param0 = invocation.getArgument(0);
      param0.get().roll();
      return 6;
    } ;
  }

  public static Answer<Integer> getAnswer4() {
    return (InvocationOnMock invocation) -> {
      Optional<OptionalExample2.Dice> param0 = invocation.getArgument(0);
      param0.get().roll();
      return 6;
    } ;
  }

  public static OptionalExample2.DiceRoller environmentNode0() throws Exception {
    OptionalExample2.DiceRoller mockObject = mock(OptionalExample2.DiceRoller.class);
    doAnswer(getAnswer0()).doAnswer(getAnswer1()).doAnswer(getAnswer2()).doAnswer(getAnswer3()).doAnswer(getAnswer4()).when(mockObject).roll(any(Optional.class));
    return mockObject;
  }

  public static OptionalExample2.DiceRoller create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

