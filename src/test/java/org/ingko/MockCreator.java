package org.ingko;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Integer;
import org.ingko.testData.MagicNumbers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;



public class MockCreator {
    public static Answer<Integer> getAnswer1() {
        return (InvocationOnMock invocation) ->  {
            return 1;
        } ;
    }

    public static MagicNumbers.MyInt environmentNode1() {
        MagicNumbers.MyInt mockObject = mock(MagicNumbers.MyInt.class);
        doAnswer(getAnswer1()).when(mockObject).getValue();
        return mockObject;
    }

    public static Answer<Integer> getAnswer2() {
        return (InvocationOnMock invocation) ->  {
            return 2;
        } ;
    }

    public static MagicNumbers.MyInt environmentNode2() {
        MagicNumbers.MyInt mockObject = mock(MagicNumbers.MyInt.class);
        doAnswer(getAnswer2()).when(mockObject).getValue();
        return mockObject;
    }

    public static Answer<Integer> getAnswer3() {
        return (InvocationOnMock invocation) ->  {
            return 4;
        } ;
    }

    public static MagicNumbers.MyInt environmentNode3() {
        MagicNumbers.MyInt mockObject = mock(MagicNumbers.MyInt.class);
        doAnswer(getAnswer3()).when(mockObject).getValue();
        return mockObject;
    }

    public static Answer<Integer> getAnswer4() {
        return (InvocationOnMock invocation) ->  {
            return 2;
        } ;
    }

    public static MagicNumbers.MyInt environmentNode4() {
        MagicNumbers.MyInt mockObject = mock(MagicNumbers.MyInt.class);
        doAnswer(getAnswer4()).when(mockObject).getValue();
        return mockObject;
    }

    public static Answer<Integer> getAnswer5() {
        return (InvocationOnMock invocation) ->  {
            return 3;
        } ;
    }

    public static MagicNumbers.MyInt environmentNode5() {
        MagicNumbers.MyInt mockObject = mock(MagicNumbers.MyInt.class);
        doAnswer(getAnswer5()).when(mockObject).getValue();
        return mockObject;
    }

    public static Answer<MagicNumbers.MyInt> getAnswer0() {
        return (InvocationOnMock invocation) ->  {
            MagicNumbers.MyInt param0 = invocation.getArgument(0);
            MagicNumbers.MyInt return0 = param0.add(environmentNode1());
            MagicNumbers.MyInt return1 = return0.times(environmentNode2());
            MagicNumbers.MyInt return2 = return1.add(environmentNode3());
            MagicNumbers.MyInt return3 = return2.divide(environmentNode4());
            MagicNumbers.MyInt return4 = return3.minus(environmentNode5());
            return return4;
        } ;
    }

    public static MagicNumbers.MathMagic environmentNode0() {
        MagicNumbers.MathMagic mockObject = mock(MagicNumbers.MathMagic.class);
        doAnswer(getAnswer0()).when(mockObject).magic(any());
        return mockObject;
    }
}