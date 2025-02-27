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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.RuntimeException;
import org.rere.core.serde.PrimitiveSerde;

public class TemplateExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static TemplateExample.TemplateDice environmentNode0() throws Exception {
    TemplateExample.TemplateDice mockObject = mock(TemplateExample.TemplateDice.class);
    Integer local0 = 1;
    Integer local1 = 1;
    Integer local2 = 1;
    Integer local3 = 1;
    Integer local4 = 1;
    doReturn(local0).doReturn(local1).doReturn(local2).doReturn(local3).doReturn(local4).when(mockObject).getObject();
    int local5 = 1;
    int local6 = 5;
    int local7 = 2;
    int local8 = 6;
    int local9 = 6;
    doReturn(local5).doReturn(local6).doReturn(local7).doReturn(local8).doReturn(local9).when(mockObject).roll();
    return mockObject;
  }

  public static TemplateExample.TemplateDice create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

