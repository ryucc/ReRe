/*
Hello World!
*/
package org.rere.examples.readmeRecursive;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;
import org.rere.core.serde.DefaultSerde;

public class ReadmeRecursiveExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static String environmentNode2() throws Exception {
    return (String) defaultSerde.deserialize("rO0ABXQADEhlbGxvIFdvcmxkIQ==");
  }

  public static ReadmeRecursiveExample.HttpResponse environmentNode1() throws Exception {
    ReadmeRecursiveExample.HttpResponse mockObject = mock(ReadmeRecursiveExample.HttpResponse.class);
    doReturn(environmentNode2()).when(mockObject).getBody();
    return mockObject;
  }

  public static ReadmeRecursiveExample.HttpClient environmentNode0() throws Exception {
    ReadmeRecursiveExample.HttpClient mockObject = mock(ReadmeRecursiveExample.HttpClient.class);
    doReturn(environmentNode1()).when(mockObject).get(anyInt());
    return mockObject;
  }

  public static ReadmeRecursiveExample.HttpClient create() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

