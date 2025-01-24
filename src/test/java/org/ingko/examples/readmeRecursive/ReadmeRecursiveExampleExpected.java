/*
Hello World!
*/
package org.ingko.examples.readmeRecursive;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.ingko.core.serde.DefaultSerde;

public class ReadmeRecursiveExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ReadmeRecursiveExample.HttpResponse environmentNode1() {
    ReadmeRecursiveExample.HttpResponse mockObject = mock(ReadmeRecursiveExample.HttpResponse.class);
    doReturn("Hello World!").when(mockObject).getBody();
    return mockObject;
  }

  public static ReadmeRecursiveExample.HttpClient environmentNode0() {
    ReadmeRecursiveExample.HttpClient mockObject = mock(ReadmeRecursiveExample.HttpClient.class);
    doReturn(environmentNode1()).when(mockObject).get(anyInt());
    return mockObject;
  }
}

