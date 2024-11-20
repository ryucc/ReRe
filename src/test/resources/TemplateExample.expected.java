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
package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import org.ingko.core.serde.DefaultSerde;
import org.ingko.examples.TemplateExample;
import org.mockito.Mockito;

public class MockCreator {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static TemplateExample.TemplateDice create() {
    TemplateExample.TemplateDice mockTemplateDice1 = Mockito.mock(TemplateExample.TemplateDice.class);
    doReturn(1).doReturn(1).doReturn(1).doReturn(1).doReturn(1).when(mockTemplateDice1).getObject();
    doReturn(1).doReturn(5).doReturn(2).doReturn(6).doReturn(6).when(mockTemplateDice1).roll();
    return mockTemplateDice1;
  }
}

