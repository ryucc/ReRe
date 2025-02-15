/*
Rolled 1
goos 1
Rolled 5
goos 1
Rolled 2
goos 1
Rolled 6
goos 1
Rolled 6
goos 1
*/
package org.rere.examples.genericTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import org.rere.core.serde.DefaultSerde;

public class TemplateExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static TemplateExample.TemplateDice environmentNode0() throws Exception {
    TemplateExample.TemplateDice mockObject = mock(TemplateExample.TemplateDice.class);
    doReturn((java.lang.Integer) 1).doReturn((java.lang.Integer) 1).doReturn((java.lang.Integer) 1).doReturn((java.lang.Integer) 1).doReturn((java.lang.Integer) 1).when(mockObject).getObject();
    doReturn((int) 1).doReturn((int) 5).doReturn((int) 2).doReturn((int) 6).doReturn((int) 6).when(mockObject).roll();
    return mockObject;
  }
}

