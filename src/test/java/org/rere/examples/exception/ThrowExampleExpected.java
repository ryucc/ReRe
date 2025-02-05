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

import java.lang.RuntimeException;
import org.rere.core.serde.DefaultSerde;

public class ThrowExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static RuntimeException environmentNode1() {
    return (RuntimeException) defaultSerde.deserialize("rO0ABXNyABpqYXZhLmxhbmcuUnVudGltZUV4Y2VwdGlvbp5fBkcKNIPlAgAAeHIAE2phdmEubGFuZy5FeGNlcHRpb27Q/R8+GjscxAIAAHhyABNqYXZhLmxhbmcuVGhyb3dhYmxl1cY1Jzl3uMsDAARMAAVjYXVzZXQAFUxqYXZhL2xhbmcvVGhyb3dhYmxlO0wADWRldGFpbE1lc3NhZ2V0ABJMamF2YS9sYW5nL1N0cmluZztbAApzdGFja1RyYWNldAAeW0xqYXZhL2xhbmcvU3RhY2tUcmFjZUVsZW1lbnQ7TAAUc3VwcHJlc3NlZEV4Y2VwdGlvbnN0ABBMamF2YS91dGlsL0xpc3Q7eHBxAH4AB3QAHmRpY2UgdGhyb3dzIG9uIDNyZCBpbnZvY2F0aW9uLnVyAB5bTGphdmEubGFuZy5TdGFja1RyYWNlRWxlbWVudDsCRio8PP0iOQIAAHhwAAAAWXNyABtqYXZhLmxhbmcuU3RhY2tUcmFjZUVsZW1lbnRhCcWaJjbdhQIACEIABmZvcm1hdEkACmxpbmVOdW1iZXJMAA9jbGFzc0xvYWRlck5hbWVxAH4ABEwADmRlY2xhcmluZ0NsYXNzcQB+AARMAAhmaWxlTmFtZXEAfgAETAAKbWV0aG9kTmFtZXEAfgAETAAKbW9kdWxlTmFtZXEAfgAETAANbW9kdWxlVmVyc2lvbnEAfgAEeHABAAAALHQAA2FwcHQAMm9yZy5yZXJlLmV4YW1wbGVzLmV4Y2VwdGlvbi5UaHJvd0V4YW1wbGUkRXJyb3JEaWNldAARVGhyb3dFeGFtcGxlLmphdmF0AARyb2xscHBzcQB+AAsCAAAAZ3B0AC9qZGsuaW50ZXJuYWwucmVmbGVjdC5EaXJlY3RNZXRob2RIYW5kbGVBY2Nlc3NvcnQAH0RpcmVjdE1ldGhvZEhhbmRsZUFjY2Vzc29yLmphdmF0AAZpbnZva2V0AAlqYXZhLmJhc2V0AAYyMS4wLjFzcQB+AAsCAAACRHB0ABhqYXZhLmxhbmcucmVmbGVjdC5NZXRob2R0AAtNZXRob2QuamF2YXEAfgAUcQB+ABVxAH4AFnNxAH4ACwEAAABOcQB+AA10ADxvcmcucmVyZS5jb3JlLmxpc3RlbmVyLmludGVyY2VwdG9yLkVudmlyb25tZW50T2JqZWN0TGlzdGVuZXJ0AB5FbnZpcm9ubWVudE9iamVjdExpc3RlbmVyLmphdmF0ABJpbnRlcmNlcHRJbnRlcmZhY2VwcHNxAH4ACwEAAAAXcQB+AA1xAH4AG3EAfgAccQB+AB1wcHNxAH4ACwEAAAA4cQB+AA10AEVvcmcucmVyZS5jb3JlLmxpc3RlbmVyLndyYXAuYnl0ZWJ1ZGR5LkVudmlyb25tZW50Tm9kZVdyYXBwZXIkTGlzdGVuZXJ0ABtFbnZpcm9ubWVudE5vZGVXcmFwcGVyLmphdmF0AAlpbnRlcmNlcHRwcHNxAH4ACwD/////cHQARW9yZy5yZXJlLmV4YW1wbGVzLmV4Y2VwdGlvbi5UaHJvd0V4YW1wbGUkRXJyb3JEaWNlJEJ5dGVCdWRkeSR0WllDdk1rMXBxAH4AEHBwc3EAfgALAQAAABNxAH4ADXQAKG9yZy5yZXJlLmV4YW1wbGVzLmV4Y2VwdGlvbi5UaHJvd0V4YW1wbGVxAH4AD3QABG1haW5wcHNxAH4ACwEAAACYcQB+AA10AB5vcmcucmVyZS5leGFtcGxlcy5FeGFtcGxlVGVzdHN0ABFFeGFtcGxlVGVzdHMuamF2YXQAEHRlc3RUaHJvd0V4YW1wbGVwcHNxAH4ACwIAAABncHEAfgAScQB+ABNxAH4AFHEAfgAVcQB+ABZzcQB+AAsCAAACRHBxAH4AGHEAfgAZcQB+ABRxAH4AFXEAfgAWc3EAfgALAQAAAv1xAH4ADXQAL29yZy5qdW5pdC5wbGF0Zm9ybS5jb21tb25zLnV0aWwuUmVmbGVjdGlvblV0aWxzdAAUUmVmbGVjdGlvblV0aWxzLmphdmF0AAxpbnZva2VNZXRob2RwcHNxAH4ACwEAAAA8cQB+AA10ADNvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXhlY3V0aW9uLk1ldGhvZEludm9jYXRpb250ABVNZXRob2RJbnZvY2F0aW9uLmphdmF0AAdwcm9jZWVkcHBzcQB+AAsBAAAAg3EAfgANdABSb3JnLmp1bml0Lmp1cGl0ZXIuZW5naW5lLmV4ZWN1dGlvbi5JbnZvY2F0aW9uSW50ZXJjZXB0b3JDaGFpbiRWYWxpZGF0aW5nSW52b2NhdGlvbnQAH0ludm9jYXRpb25JbnRlcmNlcHRvckNoYWluLmphdmFxAH4ANXBwc3EAfgALAQAAAJxxAH4ADXQAM29yZy5qdW5pdC5qdXBpdGVyLmVuZ2luZS5leHRlbnNpb24uVGltZW91dEV4dGVuc2lvbnQAFVRpbWVvdXRFeHRlbnNpb24uamF2YXEAfgAicHBzcQB+AAsBAAAAk3EAfgANcQB+ADpxAH4AO3QAF2ludGVyY2VwdFRlc3RhYmxlTWV0aG9kcHBzcQB+AAsBAAAAVnEAfgANcQB+ADpxAH4AO3QAE2ludGVyY2VwdFRlc3RNZXRob2RwcHNxAH4ACwEAAABncQB+AA10AFpvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZXhlY3V0aW9uLkludGVyY2VwdGluZ0V4ZWN1dGFibGVJbnZva2VyJFJlZmxlY3RpdmVJbnRlcmNlcHRvckNhbGx0ACJJbnRlcmNlcHRpbmdFeGVjdXRhYmxlSW52b2tlci5qYXZhdAAVbGFtYmRhJG9mVm9pZE1ldGhvZCQwcHBzcQB+AAsBAAAAXXEAfgANdABAb3JnLmp1bml0Lmp1cGl0ZXIuZW5naW5lLmV4ZWN1dGlvbi5JbnRlcmNlcHRpbmdFeGVjdXRhYmxlSW52b2tlcnEAfgBCdAAPbGFtYmRhJGludm9rZSQwcHBzcQB+AAsBAAAAanEAfgANdABTb3JnLmp1bml0Lmp1cGl0ZXIuZW5naW5lLmV4ZWN1dGlvbi5JbnZvY2F0aW9uSW50ZXJjZXB0b3JDaGFpbiRJbnRlcmNlcHRlZEludm9jYXRpb25xAH4AOHEAfgA1cHBzcQB+AAsBAAAAQHEAfgANdAA9b3JnLmp1bml0Lmp1cGl0ZXIuZW5naW5lLmV4ZWN1dGlvbi5JbnZvY2F0aW9uSW50ZXJjZXB0b3JDaGFpbnEAfgA4cQB+ADVwcHNxAH4ACwEAAAAtcQB+AA1xAH4ASnEAfgA4dAAOY2hhaW5BbmRJbnZva2VwcHNxAH4ACwEAAAAlcQB+AA1xAH4ASnEAfgA4cQB+ABRwcHNxAH4ACwEAAABccQB+AA1xAH4ARXEAfgBCcQB+ABRwcHNxAH4ACwEAAABWcQB+AA1xAH4ARXEAfgBCcQB+ABRwcHNxAH4ACwEAAADacQB+AA10ADxvcmcuanVuaXQuanVwaXRlci5lbmdpbmUuZGVzY3JpcHRvci5UZXN0TWV0aG9kVGVzdERlc2NyaXB0b3J0AB1UZXN0TWV0aG9kVGVzdERlc2NyaXB0b3IuamF2YXQAGWxhbWJkYSRpbnZva2VUZXN0TWV0aG9kJDdwcHNxAH4ACwEAAABJcQB+AA10AEFvcmcuanVuaXQucGxhdGZvcm0uZW5naW5lLnN1cHBvcnQuaGllcmFyY2hpY2FsLlRocm93YWJsZUNvbGxlY3RvcnQAF1Rocm93YWJsZUNvbGxlY3Rvci5qYXZhdAAHZXhlY3V0ZXBwc3EAfgALAQAAANZxAH4ADXEAfgBRcQB+AFJ0ABBpbnZva2VUZXN0TWV0aG9kcHBzcQB+AAsBAAAAi3EAfgANcQB+AFFxAH4AUnEAfgBXcHBzcQB+AAsBAAAARXEAfgANcQB+AFFxAH4AUnEAfgBXcHBzcQB+AAsBAAAAl3EAfgANdAA7b3JnLmp1bml0LnBsYXRmb3JtLmVuZ2luZS5zdXBwb3J0LmhpZXJhcmNoaWNhbC5Ob2RlVGVzdFRhc2t0ABFOb2RlVGVzdFRhc2suamF2YXQAG2xhbWJkYSRleGVjdXRlUmVjdXJzaXZlbHkkNnBwc3EAfgALAQAAAElxAH4ADXEAfgBVcQB+AFZxAH4AV3Bwc3EAfgALAQAAAI1xAH4ADXEAfgBdcQB+AF50ABtsYW1iZGEkZXhlY3V0ZVJlY3Vyc2l2ZWx5JDhwcHNxAH4ACwEAAACJcQB+AA10ADNvcmcuanVuaXQucGxhdGZvcm0uZW5naW5lLnN1cHBvcnQuaGllcmFyY2hpY2FsLk5vZGV0AAlOb2RlLmphdmF0AAZhcm91bmRwcHNxAH4ACwEAAACLcQB+AA1xAH4AXXEAfgBedAAbbGFtYmRhJGV4ZWN1dGVSZWN1cnNpdmVseSQ5cHBzcQB+AAsBAAAASXEAfgANcQB+AFVxAH4AVnEAfgBXcHBzcQB+AAsBAAAAinEAfgANcQB+AF1xAH4AXnQAEmV4ZWN1dGVSZWN1cnNpdmVseXBwc3EAfgALAQAAAF9xAH4ADXEAfgBdcQB+AF5xAH4AV3Bwc3EAfgALAgAABjxwdAATamF2YS51dGlsLkFycmF5TGlzdHQADkFycmF5TGlzdC5qYXZhdAAHZm9yRWFjaHEAfgAVcQB+ABZzcQB+AAsBAAAAKXEAfgANdABYb3JnLmp1bml0LnBsYXRmb3JtLmVuZ2luZS5zdXBwb3J0LmhpZXJhcmNoaWNhbC5TYW1lVGhyZWFkSGllcmFyY2hpY2FsVGVzdEV4ZWN1dG9yU2VydmljZXQALlNhbWVUaHJlYWRIaWVyYXJjaGljYWxUZXN0RXhlY3V0b3JTZXJ2aWNlLmphdmF0AAlpbnZva2VBbGxwcHNxAH4ACwEAAACbcQB+AA1xAH4AXXEAfgBecQB+AF9wcHNxAH4ACwEAAABJcQB+AA1xAH4AVXEAfgBWcQB+AFdwcHNxAH4ACwEAAACNcQB+AA1xAH4AXXEAfgBecQB+AGJwcHNxAH4ACwEAAACJcQB+AA1xAH4AZHEAfgBlcQB+AGZwcHNxAH4ACwEAAACLcQB+AA1xAH4AXXEAfgBecQB+AGhwcHNxAH4ACwEAAABJcQB+AA1xAH4AVXEAfgBWcQB+AFdwcHNxAH4ACwEAAACKcQB+AA1xAH4AXXEAfgBecQB+AGtwcHNxAH4ACwEAAABfcQB+AA1xAH4AXXEAfgBecQB+AFdwcHNxAH4ACwIAAAY8cHEAfgBucQB+AG9xAH4AcHEAfgAVcQB+ABZzcQB+AAsBAAAAKXEAfgANcQB+AHJxAH4Ac3EAfgB0cHBzcQB+AAsBAAAAm3EAfgANcQB+AF1xAH4AXnEAfgBfcHBzcQB+AAsBAAAASXEAfgANcQB+AFVxAH4AVnEAfgBXcHBzcQB+AAsBAAAAjXEAfgANcQB+AF1xAH4AXnEAfgBicHBzcQB+AAsBAAAAiXEAfgANcQB+AGRxAH4AZXEAfgBmcHBzcQB+AAsBAAAAi3EAfgANcQB+AF1xAH4AXnEAfgBocHBzcQB+AAsBAAAASXEAfgANcQB+AFVxAH4AVnEAfgBXcHBzcQB+AAsBAAAAinEAfgANcQB+AF1xAH4AXnEAfgBrcHBzcQB+AAsBAAAAX3EAfgANcQB+AF1xAH4AXnEAfgBXcHBzcQB+AAsBAAAAI3EAfgANcQB+AHJxAH4Ac3QABnN1Ym1pdHBwc3EAfgALAQAAADlxAH4ADXQAR29yZy5qdW5pdC5wbGF0Zm9ybS5lbmdpbmUuc3VwcG9ydC5oaWVyYXJjaGljYWwuSGllcmFyY2hpY2FsVGVzdEV4ZWN1dG9ydAAdSGllcmFyY2hpY2FsVGVzdEV4ZWN1dG9yLmphdmFxAH4AV3Bwc3EAfgALAQAAADZxAH4ADXQARW9yZy5qdW5pdC5wbGF0Zm9ybS5lbmdpbmUuc3VwcG9ydC5oaWVyYXJjaGljYWwuSGllcmFyY2hpY2FsVGVzdEVuZ2luZXQAG0hpZXJhcmNoaWNhbFRlc3RFbmdpbmUuamF2YXEAfgBXcHBzcQB+AAsBAAAAa3EAfgANdAA8b3JnLmp1bml0LnBsYXRmb3JtLmxhdW5jaGVyLmNvcmUuRW5naW5lRXhlY3V0aW9uT3JjaGVzdHJhdG9ydAAgRW5naW5lRXhlY3V0aW9uT3JjaGVzdHJhdG9yLmphdmFxAH4AV3Bwc3EAfgALAQAAAFhxAH4ADXEAfgCQcQB+AJFxAH4AV3Bwc3EAfgALAQAAADZxAH4ADXEAfgCQcQB+AJF0ABBsYW1iZGEkZXhlY3V0ZSQwcHBzcQB+AAsBAAAAQ3EAfgANcQB+AJBxAH4AkXQAFndpdGhJbnRlcmNlcHRlZFN0cmVhbXNwcHNxAH4ACwEAAAA0cQB+AA1xAH4AkHEAfgCRcQB+AFdwcHNxAH4ACwEAAABycQB+AA10ADBvcmcuanVuaXQucGxhdGZvcm0ubGF1bmNoZXIuY29yZS5EZWZhdWx0TGF1bmNoZXJ0ABREZWZhdWx0TGF1bmNoZXIuamF2YXEAfgBXcHBzcQB+AAsBAAAAVnEAfgANcQB+AJlxAH4AmnEAfgBXcHBzcQB+AAsBAAAAVnEAfgANdABKb3JnLmp1bml0LnBsYXRmb3JtLmxhdW5jaGVyLmNvcmUuRGVmYXVsdExhdW5jaGVyU2Vzc2lvbiREZWxlZ2F0aW5nTGF1bmNoZXJ0ABtEZWZhdWx0TGF1bmNoZXJTZXNzaW9uLmphdmFxAH4AV3Bwc3EAfgALAAAAAHdwdABxb3JnLmdyYWRsZS5hcGkuaW50ZXJuYWwudGFza3MudGVzdGluZy5qdW5pdHBsYXRmb3JtLkpVbml0UGxhdGZvcm1UZXN0Q2xhc3NQcm9jZXNzb3IkQ29sbGVjdEFsbFRlc3RDbGFzc2VzRXhlY3V0b3J0ACRKVW5pdFBsYXRmb3JtVGVzdENsYXNzUHJvY2Vzc29yLmphdmF0ABVwcm9jZXNzQWxsVGVzdENsYXNzZXNwcHNxAH4ACwAAAABecHEAfgCgcQB+AKF0AAphY2Nlc3MkMDAwcHBzcQB+AAsAAAAAWXB0AFNvcmcuZ3JhZGxlLmFwaS5pbnRlcm5hbC50YXNrcy50ZXN0aW5nLmp1bml0cGxhdGZvcm0uSlVuaXRQbGF0Zm9ybVRlc3RDbGFzc1Byb2Nlc3NvcnEAfgChdAAEc3RvcHBwc3EAfgALAAAAAD5wdAA9b3JnLmdyYWRsZS5hcGkuaW50ZXJuYWwudGFza3MudGVzdGluZy5TdWl0ZVRlc3RDbGFzc1Byb2Nlc3NvcnQAHFN1aXRlVGVzdENsYXNzUHJvY2Vzc29yLmphdmFxAH4Ap3Bwc3EAfgALAgAAAGdwcQB+ABJxAH4AE3EAfgAUcQB+ABVxAH4AFnNxAH4ACwIAAAJEcHEAfgAYcQB+ABlxAH4AFHEAfgAVcQB+ABZzcQB+AAsAAAAAJHB0AC9vcmcuZ3JhZGxlLmludGVybmFsLmRpc3BhdGNoLlJlZmxlY3Rpb25EaXNwYXRjaHQAF1JlZmxlY3Rpb25EaXNwYXRjaC5qYXZhdAAIZGlzcGF0Y2hwcHNxAH4ACwAAAAAYcHEAfgCucQB+AK9xAH4AsHBwc3EAfgALAAAAACFwdAA3b3JnLmdyYWRsZS5pbnRlcm5hbC5kaXNwYXRjaC5Db250ZXh0Q2xhc3NMb2FkZXJEaXNwYXRjaHQAH0NvbnRleHRDbGFzc0xvYWRlckRpc3BhdGNoLmphdmFxAH4AsHBwc3EAfgALAAAAAF5wdABOb3JnLmdyYWRsZS5pbnRlcm5hbC5kaXNwYXRjaC5Qcm94eURpc3BhdGNoQWRhcHRlciREaXNwYXRjaGluZ0ludm9jYXRpb25IYW5kbGVydAAZUHJveHlEaXNwYXRjaEFkYXB0ZXIuamF2YXEAfgAUcHBzcQB+AAsA/////3B0ABJqZGsucHJveHkyLiRQcm94eTVwcQB+AKd0AApqZGsucHJveHkycHNxAH4ACwAAAADBcHQAOW9yZy5ncmFkbGUuYXBpLmludGVybmFsLnRhc2tzLnRlc3Rpbmcud29ya2VyLlRlc3RXb3JrZXIkM3QAD1Rlc3RXb3JrZXIuamF2YXQAA3J1bnBwc3EAfgALAAAAAIFwdAA3b3JnLmdyYWRsZS5hcGkuaW50ZXJuYWwudGFza3MudGVzdGluZy53b3JrZXIuVGVzdFdvcmtlcnEAfgC9dAAcZXhlY3V0ZUFuZE1haW50YWluVGhyZWFkTmFtZXBwc3EAfgALAAAAAGRwcQB+AMBxAH4AvXEAfgBXcHBzcQB+AAsAAAAAPHBxAH4AwHEAfgC9cQB+AFdwcHNxAH4ACwAAAAA4cHQAPm9yZy5ncmFkbGUucHJvY2Vzcy5pbnRlcm5hbC53b3JrZXIuY2hpbGQuQWN0aW9uRXhlY3V0aW9uV29ya2VydAAaQWN0aW9uRXhlY3V0aW9uV29ya2VyLmphdmFxAH4AV3Bwc3EAfgALAAAAAHFwdABLb3JnLmdyYWRsZS5wcm9jZXNzLmludGVybmFsLndvcmtlci5jaGlsZC5TeXN0ZW1BcHBsaWNhdGlvbkNsYXNzTG9hZGVyV29ya2VydAAnU3lzdGVtQXBwbGljYXRpb25DbGFzc0xvYWRlcldvcmtlci5qYXZhdAAEY2FsbHBwc3EAfgALAAAAAEFwcQB+AMhxAH4AyXEAfgDKcHBzcQB+AAsBAAAARXEAfgANdAA6d29ya2VyLm9yZy5ncmFkbGUucHJvY2Vzcy5pbnRlcm5hbC53b3JrZXIuR3JhZGxlV29ya2VyTWFpbnQAFUdyYWRsZVdvcmtlck1haW4uamF2YXEAfgC+cHBzcQB+AAsBAAAASnEAfgANcQB+AM1xAH4AznEAfgAncHBzcgAfamF2YS51dGlsLkNvbGxlY3Rpb25zJEVtcHR5TGlzdHq4F7Q8p57eAgAAeHB4");
  }

  public static ThrowExample.ErrorDice environmentNode0() {
    ThrowExample.ErrorDice mockObject = mock(ThrowExample.ErrorDice.class);
    doReturn(2).doReturn(2).doThrow(environmentNode1()).doReturn(1).doReturn(4).when(mockObject).roll();
    return mockObject;
  }
}

