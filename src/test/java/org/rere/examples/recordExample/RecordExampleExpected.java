/*
Rolled 1
Rolled 1
Rolled 5
Rolled 5
Rolled 2
Rolled 2
Rolled 6
Rolled 6
Rolled 6
Rolled 6
*/
package org.rere.examples.recordExample;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.DefaultSerde;

public class RecordExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static RecordExample.Dice environmentNode1() throws Exception {
    RecordExample.Dice mockObject = mock(RecordExample.Dice.class);
    doReturn((int) 1).doReturn((int) 5).doReturn((int) 2).doReturn((int) 6).doReturn((int) 6).when(mockObject).roll();
    return mockObject;
  }

  public static RecordExample.Dice environmentNode2() throws Exception {
    RecordExample.Dice mockObject = mock(RecordExample.Dice.class);
    doReturn((int) 1).doReturn((int) 5).doReturn((int) 2).doReturn((int) 6).doReturn((int) 6).when(mockObject).roll();
    return mockObject;
  }

  public static RecordExample.TwoDice environmentNode0() throws Exception {
    RecordExample.TwoDice object0;
    RecordExample.Dice object1;
    RecordExample.Dice object2;
    object1 = environmentNode1();
    object2 = environmentNode2();
    object0 = new RecordExample.TwoDice(object1,object2);
    return object0;
  }

  public static RecordExample.TwoDice create() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

