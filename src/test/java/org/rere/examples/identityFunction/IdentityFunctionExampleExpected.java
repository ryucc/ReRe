package org.rere.examples.identityFunction;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.DefaultSerde;

public class IdentityFunctionExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static Answer<ArrayList> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      ArrayList param0 = invocation.getArgument(0);
      return param0;
    } ;
  }

  public static IdentityFunctionExample.IdentityFunction environmentNode0() {
    IdentityFunctionExample.IdentityFunction mockObject = mock(IdentityFunctionExample.IdentityFunction.class);
    doAnswer(getAnswer0()).when(mockObject).call(any());
    return mockObject;
  }
}

