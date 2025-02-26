package org.rere.examples.identityFunction;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.Object;
import java.lang.RuntimeException;
import java.util.ArrayList;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.PrimitiveSerde;

public class IdentityFunctionExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static Answer<Object> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      ArrayList param0 = invocation.getArgument(0);
      return param0;
    } ;
  }

  public static IdentityFunctionExample.IdentityFunction environmentNode0() throws Exception {
    IdentityFunctionExample.IdentityFunction mockObject = mock(IdentityFunctionExample.IdentityFunction.class);
    doAnswer(getAnswer0()).when(mockObject).call(any());
    return mockObject;
  }

  public static IdentityFunctionExample.IdentityFunction create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

