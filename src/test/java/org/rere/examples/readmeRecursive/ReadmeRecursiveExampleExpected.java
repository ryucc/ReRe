/*
Hello World!
*/
package org.rere.examples.readmeRecursive;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;
import org.rere.core.serde.PrimitiveSerde;

public class ReadmeRecursiveExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static String environmentNode2() throws Exception {
    // Hello World!
    return (String) defaultSerde.deserialize("rO0ABXQADEhlbGxvIFdvcmxkIQ==");
  }

  public static ReadmeRecursiveExample.HttpResponse environmentNode1() throws Exception {
    ReadmeRecursiveExample.HttpResponse mockObject = mock(ReadmeRecursiveExample.HttpResponse.class);
    String local0 = environmentNode2();
    doReturn(local0).when(mockObject).getBody();
    return mockObject;
  }

  public static ReadmeRecursiveExample.HttpClient environmentNode0() throws Exception {
    ReadmeRecursiveExample.HttpClient mockObject = mock(ReadmeRecursiveExample.HttpClient.class);
    ReadmeRecursiveExample.HttpResponse local0 = environmentNode1();
    doReturn(local0).when(mockObject).get(anyInt());
    return mockObject;
  }

  public static ReadmeRecursiveExample.HttpClient create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

