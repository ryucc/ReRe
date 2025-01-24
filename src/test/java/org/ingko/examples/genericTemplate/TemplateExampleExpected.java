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
package org.ingko.examples.templateMatching;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.ingko.core.serde.DefaultSerde;
import org.ingko.examples.genericTemplate.TemplateExample;

public class TemplateExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static TemplateExample.TemplateDice environmentNode0() {
    TemplateExample.TemplateDice mockObject = mock(TemplateExample.TemplateDice.class);
    doReturn(1).doReturn(1).doReturn(1).doReturn(1).doReturn(1).when(mockObject).getObject();
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockObject).roll();
    return mockObject;
  }
}

