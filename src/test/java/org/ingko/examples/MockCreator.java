package org.ingko.examples;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Object;
import java.util.List;
import org.ingko.examples.SortExample;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockCreator {
    public static Answer<Void> getAnswer0() {
        return (InvocationOnMock invocation) ->  {
            List param0 = invocation.getArgument(0);
            param0.size();
            param0.get(1);
            param0.get(0);
            param0.set(1, 3);
            param0.set(0, 1);
            param0.get(2);
            param0.get(1);
            param0.set(2, 3);
            param0.set(1, 2);
            param0.get(1);
            param0.get(0);
            param0.get(2);
            param0.get(1);
            param0.get(1);
            param0.get(0);
            param0.get(2);
            param0.get(1);
            return null;
        };
    }

    public static Answer<Object> getAnswer1() {
        return (InvocationOnMock invocation) ->  {
            List param0 = invocation.getArgument(0);
            param0.size();
            param0.get(1);
            param0.get(0);
            param0.get(2);
            param0.get(1);
            param0.get(1);
            param0.get(0);
            param0.get(2);
            param0.get(1);
            param0.get(1);
            param0.get(0);
            param0.get(2);
            param0.get(1);
            return null;
        };
    }

    public static SortExample.BubbleSorter environmentNode0() {
        SortExample.BubbleSorter mockObject = mock(SortExample.BubbleSorter.class);
        doAnswer(getAnswer0()).doAnswer(getAnswer1()).when(mockObject).sort(any());
        return mockObject;
    }
}
