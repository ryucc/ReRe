/*
2
3
*/
package org.rere.examples.sort;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.Void;
import java.util.ArrayList;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.PrimitiveSerde;

public class SortExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static Answer<Void> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      ArrayList param0 = invocation.getArgument(0);
      param0.size();
      SortExample.MyInt return1 = (SortExample.MyInt) param0.get(1);
      SortExample.MyInt return2 = (SortExample.MyInt) param0.get(0);
      return1.compare(return2);
      param0.set(1, return2);
      param0.set(0, return1);
      SortExample.MyInt return6 = (SortExample.MyInt) param0.get(1);
      SortExample.MyInt return7 = (SortExample.MyInt) param0.get(0);
      return6.compare(return7);
      return null;
    } ;
  }

  public static SortExample.BubbleSorter environmentNode0() throws Exception {
    SortExample.BubbleSorter mockObject = mock(SortExample.BubbleSorter.class);
    doAnswer(getAnswer0()).when(mockObject).sort(any());
    return mockObject;
  }

  public static SortExample.BubbleSorter method() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

