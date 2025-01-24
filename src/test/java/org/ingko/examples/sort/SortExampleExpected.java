/*
1
2
3
4
*/
package org.ingko.examples.sortExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Void;
import java.util.ArrayList;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.examples.sort.SortExample;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SortExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static Answer<Void> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      ArrayList param0 = invocation.getArgument(0);
      param0.size();
      param0.get(1);
      param0.get(0);
      param0.set(1, 3);
      param0.set(0, 1);
      param0.get(2);
      param0.get(1);
      param0.set(2, 3);
      param0.set(1, 2);
      param0.get(3);
      param0.get(2);
      param0.get(1);
      param0.get(0);
      param0.get(2);
      param0.get(1);
      param0.get(3);
      param0.get(2);
      param0.get(1);
      param0.get(0);
      param0.get(2);
      param0.get(1);
      param0.get(3);
      param0.get(2);
      param0.get(1);
      param0.get(0);
      param0.get(2);
      param0.get(1);
      param0.get(3);
      param0.get(2);
      return null;
    } ;
  }

  public static SortExample.BubbleSorter environmentNode0() {
    SortExample.BubbleSorter mockObject = mock(SortExample.BubbleSorter.class);
    doAnswer(getAnswer0()).when(mockObject).sort(any());
    return mockObject;
  }
}

