/*
Rolled 1
Rolled 5
Rolled 2
Rolled 6
Rolled 6
*/
package org.rere.examples.recordExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.Integer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.DefaultSerde;

public class RecordExampleExpected2 {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static Answer<Integer> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      RecordExample2.TwoDice param0 = invocation.getArgument(0);
      param0.dice1().roll();
      return 1;
    } ;
  }

  public static Answer<Integer> getAnswer1() {
    return (InvocationOnMock invocation) -> {
      RecordExample2.TwoDice param0 = invocation.getArgument(0);
      param0.dice1().roll();
      return 5;
    } ;
  }

  public static Answer<Integer> getAnswer2() {
    return (InvocationOnMock invocation) -> {
      RecordExample2.TwoDice param0 = invocation.getArgument(0);
      param0.dice1().roll();
      return 2;
    } ;
  }

  public static Answer<Integer> getAnswer3() {
    return (InvocationOnMock invocation) -> {
      RecordExample2.TwoDice param0 = invocation.getArgument(0);
      param0.dice1().roll();
      return 6;
    } ;
  }

  public static Answer<Integer> getAnswer4() {
    return (InvocationOnMock invocation) -> {
      RecordExample2.TwoDice param0 = invocation.getArgument(0);
      param0.dice1().roll();
      return 6;
    } ;
  }

  public static RecordExample2.DiceRoller environmentNode0() throws Exception {
    RecordExample2.DiceRoller mockObject = mock(RecordExample2.DiceRoller.class);
    doAnswer(getAnswer0()).doAnswer(getAnswer1()).doAnswer(getAnswer2()).doAnswer(getAnswer3()).doAnswer(getAnswer4()).when(mockObject).roll(any());
    return mockObject;
  }
}

