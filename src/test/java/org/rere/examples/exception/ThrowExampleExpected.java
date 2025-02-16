/*
Rolled 2
Rolled 2
Exception thrown.
Rolled 1
Rolled 4
*/
package org.rere.examples.exception;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import org.rere.core.serde.DefaultSerde;

public class ThrowExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static RuntimeException environmentNode1() throws Exception {
    return (RuntimeException) defaultSerde.deserialize("rO0ABXNyABpqYXZhLmxhbmcuUnVudGltZUV4Y2VwdGlvbp5fBkcKNIPlAgAAeHIAE2phdmEubGFuZy5FeGNlcHRpb27Q/R8+GjscxAIAAHhyABNqYXZhLmxhbmcuVGhyb3dhYmxl1cY1Jzl3uMsDAARMAAVjYXVzZXQAFUxqYXZhL2xhbmcvVGhyb3dhYmxlO0wADWRldGFpbE1lc3NhZ2V0ABJMamF2YS9sYW5nL1N0cmluZztbAApzdGFja1RyYWNldAAeW0xqYXZhL2xhbmcvU3RhY2tUcmFjZUVsZW1lbnQ7TAAUc3VwcHJlc3NlZEV4Y2VwdGlvbnN0ABBMamF2YS91dGlsL0xpc3Q7eHBxAH4AB3QAHmRpY2UgdGhyb3dzIG9uIDNyZCBpbnZvY2F0aW9uLnVyAB5bTGphdmEubGFuZy5TdGFja1RyYWNlRWxlbWVudDsCRio8PP0iOQIAAHhwAAAAXnNyABtqYXZhLmxhbmcuU3RhY2tUcmFjZUVsZW1lbnRhCcWaJjbdhQIACEIABmZvcm1hdEkACmxpbmVOdW1iZXJMAA9jbGFzc0xvYWRlck5hbWVxAH4ABEwADmRlY2xhcmluZ0NsYXNzcQB+AARMAAhmaWxlTmFtZXEAfgAETAAKbWV0aG9kTmFtZXEAfgAETAAKbW9kdWxlTmFtZXEAfgAETAANbW9kdWxlVmVyc2lvbnEAfgAEeHABAAAAMXQAA2FwcHQAMm9yZy5yZXJlLmV4YW1wbGVzLmV4Y2VwdGlvbi5UaHJvd0V4YW1wbGUkRXJyb3JEaWNldAARVGhyb3dFeGFtcGxlLmphdmF0AARyb2xscHBzcQB+AAsCAAAAZ3B0AC9qZGsuaW50ZXJuYWwucmVmbGVjdC5EaXJlY3RNZXRob2RIYW5kbGVBY2Nlc3NvcnQAH0RpcmVjdE1ldGhvZEhhbmRsZUFjY2Vzc29yLmphdmF0AAZpbnZva2V0AAlqYXZhLmJhc2V0AAYyMS4wLjFzcQB+AAsCAAACRHB0ABhqYXZhLmxhbmcucmVmbGVjdC5NZXRob2R0AAtNZXRob2QuamF2YXEAfgAUcQB+ABVxAH4AFnNxAH4ACwEAAABpcQB+AA10ADxvcmcucmVyZS5jb3JlLmxpc3RlbmVyLmludGVyY2VwdG9yLkVudmlyb25tZW50T2JqZWN0TGlzdGVuZXJ0AB5FbnZpcm9ubWVudE9iamVjdExpc3RlbmVyLmphdmF0ABJpbnRlcmNlcHRJbnRlcmZhY2VwcHNxAH4ACwEAAAAhcQB+AA1xAH4AG3EAfgAccQB+AB1wcHNxAH4ACwEAAABEcQB+AA10ADpvcmcucmVyZS5jb3JlLndyYXAubW9ja2l0by5Nb2NraXRvU2luZ2xlTm9kZVdyYXBwZXIkRW52QW5zdAAdTW9ja2l0b1NpbmdsZU5vZGVXcmFwcGVyLmphdmF0AAZhbnN3ZXJwcHNxAH4ACwEAAABvcQB+AA10ACxvcmcubW9ja2l0by5pbnRlcm5hbC5oYW5kbGVyLk1vY2tIYW5kbGVySW1wbHQAFE1vY2tIYW5kbGVySW1wbC5qYXZhdAAGaGFuZGxlcHBzcQB+AAsBAAAAHXEAfgANdAAvb3JnLm1vY2tpdG8uaW50ZXJuYWwuaGFuZGxlci5OdWxsUmVzdWx0R3VhcmRpYW50ABdOdWxsUmVzdWx0R3VhcmRpYW4uamF2YXEAfgAmcHBzcQB+AAsBAAAAInEAfgANdAA2b3JnLm1vY2tpdG8uaW50ZXJuYWwuaGFuZGxlci5JbnZvY2F0aW9uTm90aWZpZXJIYW5kbGVydAAeSW52b2NhdGlvbk5vdGlmaWVySGFuZGxlci5qYXZhcQB+ACZwcHNxAH4ACwEAAABScQB+AA10AD1vcmcubW9ja2l0by5pbnRlcm5hbC5jcmVhdGlvbi5ieXRlYnVkZHkuTW9ja01ldGhvZEludGVyY2VwdG9ydAAaTW9ja01ldGhvZEludGVyY2VwdG9yLmphdmF0AAtkb0ludGVyY2VwdHBwc3EAfgALAQAAAIZxAH4ADXQAOG9yZy5tb2NraXRvLmludGVybmFsLmNyZWF0aW9uLmJ5dGVidWRkeS5Nb2NrTWV0aG9kQWR2aWNldAAVTW9ja01ldGhvZEFkdmljZS5qYXZhcQB+ACZwcHNxAH4ACwEAAAAvcQB+AA1xAH4ADnEAfgAPcQB+ABBwcHNxAH4ACwEAAAAYcQB+AA10AChvcmcucmVyZS5leGFtcGxlcy5leGNlcHRpb24uVGhyb3dFeGFtcGxlcQB+AA90AARtYWlucHBzcQB+AAsBAAAAxXEAfgANdAAeb3JnLnJlcmUuZXhhbXBsZXMuRXhhbXBsZVRlc3RzdAARRXhhbXBsZVRlc3RzLmphdmF0ABB0ZXN0VGhyb3dFeGFtcGxlcHBzcQB+AAsCAAAAZ3BxAH4AEnEAfgATcQB+ABRxAH4AFXEAfgAWc3EAfgALAgAAAkRwcQB+ABhxAH4AGXEAfgAUcQB+ABVxAH4AFnNxAH4ACwEAAAL/cQB+AA10AC9vcmcuanVuaXQucGxhdGZvcm0uY29tbW9ucy51dGlsLlJlZmxlY3Rpb25VdGlsc3QAFFJlZmxlY3Rpb25VdGlscy5qYXZhdAAMaW52b2tlTWV0aG9kcHBzcQB+AAsBAAAAPHEAfgANdAAzb3JnLmp1bml0Lmp1cGl0ZXIuZW5naW5lLmV4ZWN1dGlvbi5NZXRob2RJbnZvY2F0aW9udAAVTWV0aG9kSW52b2NhdGlvbi5qYXZhdAAHcHJvY2VlZHBwc3EAfgALAQAAAINxAH4ADXQAUm9yZy5qdW5pdC5qdXBpdGVyLmVuZ2luZS5leGVjdXRpb24uSW52b2NhdGlvbkludGVyY2VwdG9yQ2hhaW4kVmFsaWRhdGluZ0ludm9jYXRpb250AB9JbnZvY2F0aW9uSW50ZXJjZXB0b3JDaGFpbi5qYXZhcQB+AEVwcHNxAH4ACwEAAACccQB+AA10ADNvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXh0ZW5zaW9uLlRpbWVvdXRFeHRlbnNpb250ABVUaW1lb3V0RXh0ZW5zaW9uLmphdmF0AAlpbnRlcmNlcHRwcHNxAH4ACwEAAACTcQB+AA1xAH4ASnEAfgBLdAAXaW50ZXJjZXB0VGVzdGFibGVNZXRob2RwcHNxAH4ACwEAAABWcQB+AA1xAH4ASnEAfgBLdAATaW50ZXJjZXB0VGVzdE1ldGhvZHBwc3EAfgALAQAAAGdxAH4ADXQAWm9yZy5qdW5pdC5qdXBpdGVyLmVuZ2luZS5leGVjdXRpb24uSW50ZXJjZXB0aW5nRXhlY3V0YWJsZUludm9rZXIkUmVmbGVjdGl2ZUludGVyY2VwdG9yQ2FsbHQAIkludGVyY2VwdGluZ0V4ZWN1dGFibGVJbnZva2VyLmphdmF0ABVsYW1iZGEkb2ZWb2lkTWV0aG9kJDBwcHNxAH4ACwEAAABdcQB+AA10AEBvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXhlY3V0aW9uLkludGVyY2VwdGluZ0V4ZWN1dGFibGVJbnZva2VycQB+AFN0AA9sYW1iZGEkaW52b2tlJDBwcHNxAH4ACwEAAABqcQB+AA10AFNvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXhlY3V0aW9uLkludm9jYXRpb25JbnRlcmNlcHRvckNoYWluJEludGVyY2VwdGVkSW52b2NhdGlvbnEAfgBIcQB+AEVwcHNxAH4ACwEAAABAcQB+AA10AD1vcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXhlY3V0aW9uLkludm9jYXRpb25JbnRlcmNlcHRvckNoYWlucQB+AEhxAH4ARXBwc3EAfgALAQAAAC1xAH4ADXEAfgBbcQB+AEh0AA5jaGFpbkFuZEludm9rZXBwc3EAfgALAQAAACVxAH4ADXEAfgBbcQB+AEhxAH4AFHBwc3EAfgALAQAAAFxxAH4ADXEAfgBWcQB+AFNxAH4AFHBwc3EAfgALAQAAAFZxAH4ADXEAfgBWcQB+AFNxAH4AFHBwc3EAfgALAQAAANlxAH4ADXQAPG9yZy5qdW5pdC5qdXBpdGVyLmVuZ2luZS5kZXNjcmlwdG9yLlRlc3RNZXRob2RUZXN0RGVzY3JpcHRvcnQAHVRlc3RNZXRob2RUZXN0RGVzY3JpcHRvci5qYXZhdAAZbGFtYmRhJGludm9rZVRlc3RNZXRob2QkOHBwc3EAfgALAQAAAElxAH4ADXQAQW9yZy5qdW5pdC5wbGF0Zm9ybS5lbmdpbmUuc3VwcG9ydC5oaWVyYXJjaGljYWwuVGhyb3dhYmxlQ29sbGVjdG9ydAAXVGhyb3dhYmxlQ29sbGVjdG9yLmphdmF0AAdleGVjdXRlcHBzcQB+AAsBAAAA1XEAfgANcQB+AGJxAH4AY3QAEGludm9rZVRlc3RNZXRob2RwcHNxAH4ACwEAAACKcQB+AA1xAH4AYnEAfgBjcQB+AGhwcHNxAH4ACwEAAABEcQB+AA1xAH4AYnEAfgBjcQB+AGhwcHNxAH4ACwEAAACccQB+AA10ADtvcmcuanVuaXQucGxhdGZvcm0uZW5naW5lLnN1cHBvcnQuaGllcmFyY2hpY2FsLk5vZGVUZXN0VGFza3QAEU5vZGVUZXN0VGFzay5qYXZh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AH4AaHBwc3EAfgALAQAAAHJxAH4ADXQAMG9yZy5qdW5pdC5wbGF0Zm9ybS5sYXVuY2hlci5jb3JlLkRlZmF1bHRMYXVuY2hlcnQAFERlZmF1bHRMYXVuY2hlci5qYXZhcQB+AGhwcHNxAH4ACwEAAABWcQB+AA1xAH4AqnEAfgCrcQB+AGhwcHNxAH4ACwEAAABWcQB+AA10AEpvcmcuanVuaXQucGxhdGZvcm0ubGF1bmNoZXIuY29yZS5EZWZhdWx0TGF1bmNoZXJTZXNzaW9uJERlbGVnYXRpbmdMYXVuY2hlcnQAG0RlZmF1bHRMYXVuY2hlclNlc3Npb24uamF2YXEAfgBocHBzcQB+AAsAAAAAd3B0AHFvcmcuZ3JhZGxlLmFwaS5pbnRlcm5hbC50YXNrcy50ZXN0aW5nLmp1bml0cGxhdGZvcm0uSlVuaXRQbGF0Zm9ybVRlc3RDbGFzc1Byb2Nlc3NvciRDb2xsZWN0QWxsVGVzdENsYXNzZXNFeGVjdXRvcnQAJEpVbml0UGxhdGZvcm1UZXN0Q2xhc3NQcm9jZXNzb3IuamF2YXQAFXByb2Nlc3NBbGxUZXN0Q2xhc3Nlc3Bwc3EAfgALAAAAAF5wcQB+ALFxAH4AsnQACmFjY2VzcyQwMDBwcHNxAH4ACwAAAABZcHQAU29yZy5ncmFkbGUuYXBpLmludGVybmFsLnRhc2tzLnRlc3RpbmcuanVuaXRwbGF0Zm9ybS5KVW5pdFBsYXRmb3JtVGVzdENsYXNzUHJvY2Vzc29ycQB+ALJ0AARzdG9wcHBzcQB+AAsAAAAAPnB0AD1vcmcuZ3JhZGxlLmFwaS5pbnRlcm5hbC50YXNrcy50ZXN0aW5nLlN1aXRlVGVzdENsYXNzUHJvY2Vzc29ydAAcU3VpdGVUZXN0Q2xhc3NQcm9jZXNzb3IuamF2YXEAfgC4cHBzcQB+AAsCAAAAZ3BxAH4AEnEAfgATcQB+ABRxAH4AFXEAfgAWc3EAfgALAgAAAkRwcQB+ABhxAH4AGXEAfgAUcQB+ABVxAH4AFnNxAH4ACwAAAAAkcHQAL29yZy5ncmFkbGUuaW50ZXJuYWwuZGlzcGF0Y2guUmVmbGVjdGlvbkRpc3BhdGNodAAXUmVmbGVjdGlvbkRpc3BhdGNoLmphdmF0AAhkaXNwYXRjaHBwc3EAfgALAAAAABhwcQB+AL9xAH4AwHEAfgDBcHBzcQB+AAsAAAAAIXB0ADdvcmcuZ3JhZGxlLmludGVybmFsLmRpc3BhdGNoLkNvbnRleHRDbGFzc0xvYWRlckRpc3BhdGNodAAfQ29udGV4dENsYXNzTG9hZGVyRGlzcGF0Y2guamF2YXEAfgDBcHBzcQB+AAsAAAAAXnB0AE5vcmcuZ3JhZGxlLmludGVybmFsLmRpc3BhdGNoLlByb3h5RGlzcGF0Y2hBZGFwdGVyJERpc3BhdGNoaW5nSW52b2NhdGlvbkhhbmRsZXJ0ABlQcm94eURpc3BhdGNoQWRhcHRlci5qYXZhcQB+ABRwcHNxAH4ACwD/////cHQAEmpkay5wcm94eTIuJFByb3h5NXBxAH4AuHQACmpkay5wcm94eTJwc3EAfgALAAAAAMFwdAA5b3JnLmdyYWRsZS5hcGkuaW50ZXJuYWwudGFza3MudGVzdGluZy53b3JrZXIuVGVzdFdvcmtlciQzdAAPVGVzdFdvcmtlci5qYXZhdAADcnVucHBzcQB+AAsAAAAAgXB0ADdvcmcuZ3JhZGxlLmFwaS5pbnRlcm5hbC50YXNrcy50ZXN0aW5nLndvcmtlci5UZXN0V29ya2VycQB+AM50ABxleGVjdXRlQW5kTWFpbnRhaW5UaHJlYWROYW1lcHBzcQB+AAsAAAAAZHBxAH4A0XEAfgDOcQB+AGhwcHNxAH4ACwAAAAA8cHEAfgDRcQB+AM5xAH4AaHBwc3EAfgALAAAAADhwdAA+b3JnLmdyYWRsZS5wcm9jZXNzLmludGVybmFsLndvcmtlci5jaGlsZC5BY3Rpb25FeGVjdXRpb25Xb3JrZXJ0ABpBY3Rpb25FeGVjdXRpb25Xb3JrZXIuamF2YXEAfgBocHBzcQB+AAsAAAAAcXB0AEtvcmcuZ3JhZGxlLnByb2Nlc3MuaW50ZXJuYWwud29ya2VyLmNoaWxkLlN5c3RlbUFwcGxpY2F0aW9uQ2xhc3NMb2FkZXJXb3JrZXJ0ACdTeXN0ZW1BcHBsaWNhdGlvbkNsYXNzTG9hZGVyV29ya2VyLmphdmF0AARjYWxscHBzcQB+AAsAAAAAQXBxAH4A2XEAfgDacQB+ANtwcHNxAH4ACwEAAABFcQB+AA10ADp3b3JrZXIub3JnLmdyYWRsZS5wcm9jZXNzLmludGVybmFsLndvcmtlci5HcmFkbGVXb3JrZXJNYWludAAVR3JhZGxlV29ya2VyTWFpbi5qYXZhcQB+AM9wcHNxAH4ACwEAAABKcQB+AA1xAH4A3nEAfgDfcQB+ADdwcHNyAB9qYXZhLnV0aWwuQ29sbGVjdGlvbnMkRW1wdHlMaXN0ergXtDynnt4CAAB4cHg=");
  }

  public static ThrowExample.ErrorDice environmentNode0() throws Exception {
    ThrowExample.ErrorDice mockObject = mock(ThrowExample.ErrorDice.class);
    doReturn((int) 2).doReturn((int) 2).doThrow(environmentNode1()).doReturn((int) 1).doReturn((int) 4).when(mockObject).roll();
    return mockObject;
  }

  public static ThrowExample.ErrorDice create() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

