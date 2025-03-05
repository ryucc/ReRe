/*
Rolled 4
Rolled 5
Rolled 2
Rolled 4
Rolled 3
*/
package org.rere.examples.finalTracing;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.RuntimeException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.PrimitiveSerde;

public class FinalTracingExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static Answer<Integer> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      FinalTracingExample.Dice param0 = invocation.getArgument(0);
      // Failed node
      /*
       * ReRe cannot spy on final class: class org.rere.examples.finalTracing.FinalTracingExample$MyInt
       * Further method tracing maybe incorrect on this object.
       * If this is a environment object, consider using custom serialization.
      */
      FinalTracingExample.MyInt param1 = invocation.getArgument(1);
      param0.roll(param1);
      return 4;
    } ;
  }

  public static Answer<Integer> getAnswer1() {
    return (InvocationOnMock invocation) -> {
      FinalTracingExample.Dice param0 = invocation.getArgument(0);
      // Failed node
      /*
       * ReRe cannot spy on final class: class org.rere.examples.finalTracing.FinalTracingExample$MyInt
       * Further method tracing maybe incorrect on this object.
       * If this is a environment object, consider using custom serialization.
      */
      FinalTracingExample.MyInt param1 = invocation.getArgument(1);
      param0.roll(param1);
      return 5;
    } ;
  }

  public static Answer<Integer> getAnswer2() {
    return (InvocationOnMock invocation) -> {
      FinalTracingExample.Dice param0 = invocation.getArgument(0);
      // Failed node
      /*
       * ReRe cannot spy on final class: class org.rere.examples.finalTracing.FinalTracingExample$MyInt
       * Further method tracing maybe incorrect on this object.
       * If this is a environment object, consider using custom serialization.
      */
      FinalTracingExample.MyInt param1 = invocation.getArgument(1);
      param0.roll(param1);
      return 2;
    } ;
  }

  public static Answer<Integer> getAnswer3() {
    return (InvocationOnMock invocation) -> {
      FinalTracingExample.Dice param0 = invocation.getArgument(0);
      // Failed node
      /*
       * ReRe cannot spy on final class: class org.rere.examples.finalTracing.FinalTracingExample$MyInt
       * Further method tracing maybe incorrect on this object.
       * If this is a environment object, consider using custom serialization.
      */
      FinalTracingExample.MyInt param1 = invocation.getArgument(1);
      param0.roll(param1);
      return 4;
    } ;
  }

  public static Answer<Integer> getAnswer4() {
    return (InvocationOnMock invocation) -> {
      FinalTracingExample.Dice param0 = invocation.getArgument(0);
      // Failed node
      /*
       * ReRe cannot spy on final class: class org.rere.examples.finalTracing.FinalTracingExample$MyInt
       * Further method tracing maybe incorrect on this object.
       * If this is a environment object, consider using custom serialization.
      */
      FinalTracingExample.MyInt param1 = invocation.getArgument(1);
      param0.roll(param1);
      return 3;
    } ;
  }

  public static FinalTracingExample.DiceRoller environmentNode0() throws Exception {
    FinalTracingExample.DiceRoller mockObject = mock(FinalTracingExample.DiceRoller.class);
    doAnswer(getAnswer0()).doAnswer(getAnswer1()).doAnswer(getAnswer2()).doAnswer(getAnswer3()).doAnswer(getAnswer4()).when(mockObject).roll(any(FinalTracingExample.Dice.class), any(FinalTracingExample.MyInt.class));
    return mockObject;
  }

  public static FinalTracingExample.DiceRoller create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

