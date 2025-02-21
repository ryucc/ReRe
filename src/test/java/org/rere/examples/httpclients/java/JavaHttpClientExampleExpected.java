/*
<!DOCTYPE html>
<html id="home" lang="en">
<head>
  <title>MIT License</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta name="description"
    content="The MIT License is a permissive free software license originating at the Massachusetts Institute of Technology. As a permissive license, it puts only very limited restriction on reuse and has, therefore, an excellent license compatibility.">
  <!--
  Welcome fellow open source developer. This project is here for you to
  link to if you're like me and keep forgetting to include the
  MIT-license.txt file.

  Fork this project and send a pull request on:

  https://github.com/remy/mit-license

  By adding a new JSON file to the users directory, it will yield an
  MIT License on a CNAME, for example:

  { "copyright": "Remy Sharp, https://remysharp.com" }

  Means visiting https://rem.mit-license.org/ shows "Remy Sharp" as the
  copyright holder. Namespaces will be on a first come first serve basis,
  and I'm open to folk joining the GitHub project.

  For more options see the README in the github hosted project.

  Hope you find this useful too!

  - @rem
-->
  <script>
    document.createElement('article');
    document.createElement('footer');
  </script>
  <link rel="stylesheet" href="/themes/default.css">
</head>


<body>
  <article>
    
    <h1>The MIT License (MIT)</h1>

    <p>Copyright © 2025 &lt;copyright holders&gt;</p>

    <p>Permission is hereby granted, free of charge, to any person obtaining a copy
      of this software and associated documentation files (the “Software”), to deal
      in the Software without restriction, including without limitation the rights
      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      copies of the Software, and to permit persons to whom the Software is
      furnished to do so, subject to the following conditions:</p>

    <p>The above copyright notice and this permission notice shall be included in
      all copies or substantial portions of the Software.</p>

    <p>THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      THE SOFTWARE.</p>
  </article>
  <footer>
  <p><a target="_blank" rel="noopener" href="https://github.com/remy/mit-license">Fork this project to create your own
      MIT license that you can always link to.</a></p>
</footer>

</body>

</html>

*/
package org.rere.examples.httpclients.java;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.java.net.http.HttpHeadersSerde;

public class JavaHttpClientExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static HttpHeaders environmentNode2() throws Exception {
    // java.net.http.HttpHeaders@ae12bd69 { {access-control-allow-origin=[*], alt-svc=[h3=":443"; ma=86400], cf-cache-status=[DYNAMIC], cf-ray=[9156318b3e59ec54-SEA], connection=[keep-alive], content-type=[text/html; charset=utf-8], date=[Fri, 21 Feb 2025 10:53:36 GMT], nel=[{"report_to":"heroku-nel","max_age":3600,"success_fraction":0.005,"failure_fraction":0.05,"response_headers":["Via"]}], report-to=[{"group":"heroku-nel","max_age":3600,"endpoints":[{"url":"https://nel.heroku.com/reports?ts=1740135216&sid=af571f24-03ee-46d1-9f90-ab9030c2c74c&s=pEnM8aBpU6jQDFPrNPH6MYojKGCTMeSjh%2Fw1VBBmJIE%3D"}]}], reporting-endpoints=[heroku-nel=https://nel.heroku.com/reports?ts=1740135216&sid=af571f24-03ee-46d1-9f90-ab9030c2c74c&s=pEnM8aBpU6jQDFPrNPH6MYojKGCTMeSjh%2Fw1VBBmJIE%3D], server=[cloudflare], server-timing=[cfL4;desc="?proto=TCP&rtt=23597&min_rtt=20295&rtt_var=9969&sent=6&recv=7&lost=0&retrans=0&sent_bytes=3123&recv_bytes=676&delivery_rate=214042&cwnd=215&unsent_bytes=0&cid=e8b9942ba940dbd9&ts=366&x=0"], transfer-encoding=[chunked], via=[1.1 vegur], x-powered-by=[Express]} }
    return (HttpHeaders) new HttpHeadersSerde().deserialize("rO0ABXNyACVqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlTWFw8aWo/nT1B0ICAAFMAAFtdAAPTGphdmEvdXRpbC9NYXA7eHBzcgARamF2YS51dGlsLlRyZWVNYXAMwfY+LSVq5gMAAUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7eHBzcgAqamF2YS5sYW5nLlN0cmluZyRDYXNlSW5zZW5zaXRpdmVDb21wYXJhdG9ydwNcfVxQ5c4CAAB4cHcEAAAAD3QAG2FjY2Vzcy1jb250cm9sLWFsbG93LW9yaWdpbnNyABFqYXZhLnV0aWwuQ29sbFNlcleOq7Y6G6gRAwABSQADdGFneHAAAAABdwQAAAABdAABKnh0AAdhbHQtc3Zjc3EAfgAJAAAAAXcEAAAAAXQAE2gzPSI6NDQzIjsgbWE9ODY0MDB4dAAPY2YtY2FjaGUtc3RhdHVzc3EAfgAJAAAAAXcEAAAAAXQAB0RZTkFNSUN4dAAGY2YtcmF5c3EAfgAJAAAAAXcEAAAAAXQAFDkxNTYzMThiM2U1OWVjNTQtU0VBeHQACmNvbm5lY3Rpb25zcQB+AAkAAAABdwQAAAABdAAKa2VlcC1hbGl2ZXh0AAxjb250ZW50LXR5cGVzcQB+AAkAAAABdwQAAAABdAAYdGV4dC9odG1sOyBjaGFyc2V0PXV0Zi04eHQABGRhdGVzcQB+AAkAAAABdwQAAAABdAAdRnJpLCAyMSBGZWIgMjAyNSAxMDo1MzozNiBHTVR4dAADbmVsc3EAfgAJAAAAAXcEAAAAAXQAdXsicmVwb3J0X3RvIjoiaGVyb2t1LW5lbCIsIm1heF9hZ2UiOjM2MDAsInN1Y2Nlc3NfZnJhY3Rpb24iOjAuMDA1LCJmYWlsdXJlX2ZyYWN0aW9uIjowLjA1LCJyZXNwb25zZV9oZWFkZXJzIjpbIlZpYSJdfXh0AAlyZXBvcnQtdG9zcQB+AAkAAAABdwQAAAABdADGeyJncm91cCI6Imhlcm9rdS1uZWwiLCJtYXhfYWdlIjozNjAwLCJlbmRwb2ludHMiOlt7InVybCI6Imh0dHBzOi8vbmVsLmhlcm9rdS5jb20vcmVwb3J0cz90cz0xNzQwMTM1MjE2JnNpZD1hZjU3MWYyNC0wM2VlLTQ2ZDEtOWY5MC1hYjkwMzBjMmM3NGMmcz1wRW5NOGFCcFU2alFERlByTlBINk1Zb2pLR0NUTWVTamglMkZ3MVZCQm1KSUUlM0QifV19eHQAE3JlcG9ydGluZy1lbmRwb2ludHNzcQB+AAkAAAABdwQAAAABdACTaGVyb2t1LW5lbD1odHRwczovL25lbC5oZXJva3UuY29tL3JlcG9ydHM/dHM9MTc0MDEzNTIxNiZzaWQ9YWY1NzFmMjQtMDNlZS00NmQxLTlmOTAtYWI5MDMwYzJjNzRjJnM9cEVuTThhQnBVNmpRREZQck5QSDZNWW9qS0dDVE1lU2poJTJGdzFWQkJtSklFJTNEeHQABnNlcnZlcnNxAH4ACQAAAAF3BAAAAAF0AApjbG91ZGZsYXJleHQADXNlcnZlci10aW1pbmdzcQB+AAkAAAABdwQAAAABdADGY2ZMNDtkZXNjPSI/cHJvdG89VENQJnJ0dD0yMzU5NyZtaW5fcnR0PTIwMjk1JnJ0dF92YXI9OTk2OSZzZW50PTYmcmVjdj03Jmxvc3Q9MCZyZXRyYW5zPTAmc2VudF9ieXRlcz0zMTIzJnJlY3ZfYnl0ZXM9Njc2JmRlbGl2ZXJ5X3JhdGU9MjE0MDQyJmN3bmQ9MjE1JnVuc2VudF9ieXRlcz0wJmNpZD1lOGI5OTQyYmE5NDBkYmQ5JnRzPTM2NiZ4PTAieHQAEXRyYW5zZmVyLWVuY29kaW5nc3EAfgAJAAAAAXcEAAAAAXQAB2NodW5rZWR4dAADdmlhc3EAfgAJAAAAAXcEAAAAAXQACTEuMSB2ZWd1cnh0AAx4LXBvd2VyZWQtYnlzcQB+AAkAAAABdwQAAAABdAAHRXhwcmVzc3h4");
  }

  public static HttpResponse.ResponseInfo environmentNode1() throws Exception {
    HttpResponse.ResponseInfo mockObject = mock(HttpResponse.ResponseInfo.class);
    doReturn(environmentNode2()).when(mockObject).headers();
    return mockObject;
  }

  public static String environmentNode4() throws Exception {
    /*
     * <!DOCTYPE html>
     * <html id="home" lang="en">
     * <head>
     *   <title>MIT License</title>
     *   <meta charset="UTF-8">
     *   <meta name="viewport" content="width=device-width, initial-scale=1.0">
     *   <meta http-equiv="X-UA-Compatible" content="ie=edge">
     *   <meta name="description"
     *     content="The MIT License is a permissive free software license originating at the Massachusetts Institute of Technology. As a permissive license, it puts only very limited restriction on reuse and has, therefore, an excellent license compatibility.">
     *   <!--
     *   Welcome fellow open source developer. This project is here for you to
     *   link to if you're like me and keep forgetting to include the
     *   MIT-license.txt file.
     * 
     *   Fork this project and send a pull request on:
     * 
     *   https://github.com/remy/mit-license
     * 
     *   By adding a new JSON file to the users directory, it will yield an
     *   MIT License on a CNAME, for example:
     * 
     *   { "copyright": "Remy Sharp, https://remysharp.com" }
     * 
     *   Means visiting https://rem.mit-license.org/ shows "Remy Sharp" as the
     *   copyright holder. Namespaces will be on a first come first serve basis,
     *   and I'm open to folk joining the GitHub project.
     * 
     *   For more options see the README in the github hosted project.
     * 
     *   Hope you find this useful too!
     * 
     *   - @rem
     * -->
     *   <script>
     *     document.createElement('article');
     *     document.createElement('footer');
     *   </script>
     *   <link rel="stylesheet" href="/themes/default.css">
     * </head>
     * 
     * 
     * <body>
     *   <article>
     *     
     *     <h1>The MIT License (MIT)</h1>
     * 
     *     <p>Copyright © 2025 &lt;copyright holders&gt;</p>
     * 
     *     <p>Permission is hereby granted, free of charge, to any person obtaining a copy
     *       of this software and associated documentation files (the “Software”), to deal
     *       in the Software without restriction, including without limitation the rights
     *       to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     *       copies of the Software, and to permit persons to whom the Software is
     *       furnished to do so, subject to the following conditions:</p>
     * 
     *     <p>The above copyright notice and this permission notice shall be included in
     *       all copies or substantial portions of the Software.</p>
     * 
     *     <p>THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     *       IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     *       FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     *       AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     *       LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     *       OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
     *       THE SOFTWARE.</p>
     *   </article>
     *   <footer>
     *   <p><a target="_blank" rel="noopener" href="https://github.com/remy/mit-license">Fork this project to create your own
     *       MIT license that you can always link to.</a></p>
     * </footer>
     * 
     * </body>
     * 
     * </html>
    */
    return (String) defaultSerde.deserialize("rO0ABXQLRjwhRE9DVFlQRSBodG1sPgo8aHRtbCBpZD0iaG9tZSIgbGFuZz0iZW4iPgo8aGVhZD4KICA8dGl0bGU+TUlUIExpY2Vuc2U8L3RpdGxlPgogIDxtZXRhIGNoYXJzZXQ9IlVURi04Ij4KICA8bWV0YSBuYW1lPSJ2aWV3cG9ydCIgY29udGVudD0id2lkdGg9ZGV2aWNlLXdpZHRoLCBpbml0aWFsLXNjYWxlPTEuMCI+CiAgPG1ldGEgaHR0cC1lcXVpdj0iWC1VQS1Db21wYXRpYmxlIiBjb250ZW50PSJpZT1lZGdlIj4KICA8bWV0YSBuYW1lPSJkZXNjcmlwdGlvbiIKICAgIGNvbnRlbnQ9IlRoZSBNSVQgTGljZW5zZSBpcyBhIHBlcm1pc3NpdmUgZnJlZSBzb2Z0d2FyZSBsaWNlbnNlIG9yaWdpbmF0aW5nIGF0IHRoZSBNYXNzYWNodXNldHRzIEluc3RpdHV0ZSBvZiBUZWNobm9sb2d5LiBBcyBhIHBlcm1pc3NpdmUgbGljZW5zZSwgaXQgcHV0cyBvbmx5IHZlcnkgbGltaXRlZCByZXN0cmljdGlvbiBvbiByZXVzZSBhbmQgaGFzLCB0aGVyZWZvcmUsIGFuIGV4Y2VsbGVudCBsaWNlbnNlIGNvbXBhdGliaWxpdHkuIj4KICA8IS0tCiAgV2VsY29tZSBmZWxsb3cgb3BlbiBzb3VyY2UgZGV2ZWxvcGVyLiBUaGlzIHByb2plY3QgaXMgaGVyZSBmb3IgeW91IHRvCiAgbGluayB0byBpZiB5b3UncmUgbGlrZSBtZSBhbmQga2VlcCBmb3JnZXR0aW5nIHRvIGluY2x1ZGUgdGhlCiAgTUlULWxpY2Vuc2UudHh0IGZpbGUuCgogIEZvcmsgdGhpcyBwcm9qZWN0IGFuZCBzZW5kIGEgcHVsbCByZXF1ZXN0IG9uOgoKICBodHRwczovL2dpdGh1Yi5jb20vcmVteS9taXQtbGljZW5zZQoKICBCeSBhZGRpbmcgYSBuZXcgSlNPTiBmaWxlIHRvIHRoZSB1c2VycyBkaXJlY3RvcnksIGl0IHdpbGwgeWllbGQgYW4KICBNSVQgTGljZW5zZSBvbiBhIENOQU1FLCBmb3IgZXhhbXBsZToKCiAgeyAiY29weXJpZ2h0IjogIlJlbXkgU2hhcnAsIGh0dHBzOi8vcmVteXNoYXJwLmNvbSIgfQoKICBNZWFucyB2aXNpdGluZyBodHRwczovL3JlbS5taXQtbGljZW5zZS5vcmcvIHNob3dzICJSZW15IFNoYXJwIiBhcyB0aGUKICBjb3B5cmlnaHQgaG9sZGVyLiBOYW1lc3BhY2VzIHdpbGwgYmUgb24gYSBmaXJzdCBjb21lIGZpcnN0IHNlcnZlIGJhc2lzLAogIGFuZCBJJ20gb3BlbiB0byBmb2xrIGpvaW5pbmcgdGhlIEdpdEh1YiBwcm9qZWN0LgoKICBGb3IgbW9yZSBvcHRpb25zIHNlZSB0aGUgUkVBRE1FIGluIHRoZSBnaXRodWIgaG9zdGVkIHByb2plY3QuCgogIEhvcGUgeW91IGZpbmQgdGhpcyB1c2VmdWwgdG9vIQoKICAtIEByZW0KLS0+CiAgPHNjcmlwdD4KICAgIGRvY3VtZW50LmNyZWF0ZUVsZW1lbnQoJ2FydGljbGUnKTsKICAgIGRvY3VtZW50LmNyZWF0ZUVsZW1lbnQoJ2Zvb3RlcicpOwogIDwvc2NyaXB0PgogIDxsaW5rIHJlbD0ic3R5bGVzaGVldCIgaHJlZj0iL3RoZW1lcy9kZWZhdWx0LmNzcyI+CjwvaGVhZD4KCgo8Ym9keT4KICA8YXJ0aWNsZT4KICAgIAogICAgPGgxPlRoZSBNSVQgTGljZW5zZSAoTUlUKTwvaDE+CgogICAgPHA+Q29weXJpZ2h0IMKpIDIwMjUgJmx0O2NvcHlyaWdodCBob2xkZXJzJmd0OzwvcD4KCiAgICA8cD5QZXJtaXNzaW9uIGlzIGhlcmVieSBncmFudGVkLCBmcmVlIG9mIGNoYXJnZSwgdG8gYW55IHBlcnNvbiBvYnRhaW5pbmcgYSBjb3B5CiAgICAgIG9mIHRoaXMgc29mdHdhcmUgYW5kIGFzc29jaWF0ZWQgZG9jdW1lbnRhdGlvbiBmaWxlcyAodGhlIOKAnFNvZnR3YXJl4oCdKSwgdG8gZGVhbAogICAgICBpbiB0aGUgU29mdHdhcmUgd2l0aG91dCByZXN0cmljdGlvbiwgaW5jbHVkaW5nIHdpdGhvdXQgbGltaXRhdGlvbiB0aGUgcmlnaHRzCiAgICAgIHRvIHVzZSwgY29weSwgbW9kaWZ5LCBtZXJnZSwgcHVibGlzaCwgZGlzdHJpYnV0ZSwgc3VibGljZW5zZSwgYW5kL29yIHNlbGwKICAgICAgY29waWVzIG9mIHRoZSBTb2Z0d2FyZSwgYW5kIHRvIHBlcm1pdCBwZXJzb25zIHRvIHdob20gdGhlIFNvZnR3YXJlIGlzCiAgICAgIGZ1cm5pc2hlZCB0byBkbyBzbywgc3ViamVjdCB0byB0aGUgZm9sbG93aW5nIGNvbmRpdGlvbnM6PC9wPgoKICAgIDxwPlRoZSBhYm92ZSBjb3B5cmlnaHQgbm90aWNlIGFuZCB0aGlzIHBlcm1pc3Npb24gbm90aWNlIHNoYWxsIGJlIGluY2x1ZGVkIGluCiAgICAgIGFsbCBjb3BpZXMgb3Igc3Vic3RhbnRpYWwgcG9ydGlvbnMgb2YgdGhlIFNvZnR3YXJlLjwvcD4KCiAgICA8cD5USEUgU09GVFdBUkUgSVMgUFJPVklERUQg4oCcQVMgSVPigJ0sIFdJVEhPVVQgV0FSUkFOVFkgT0YgQU5ZIEtJTkQsIEVYUFJFU1MgT1IKICAgICAgSU1QTElFRCwgSU5DTFVESU5HIEJVVCBOT1QgTElNSVRFRCBUTyBUSEUgV0FSUkFOVElFUyBPRiBNRVJDSEFOVEFCSUxJVFksCiAgICAgIEZJVE5FU1MgRk9SIEEgUEFSVElDVUxBUiBQVVJQT1NFIEFORCBOT05JTkZSSU5HRU1FTlQuIElOIE5PIEVWRU5UIFNIQUxMIFRIRQogICAgICBBVVRIT1JTIE9SIENPUFlSSUdIVCBIT0xERVJTIEJFIExJQUJMRSBGT1IgQU5ZIENMQUlNLCBEQU1BR0VTIE9SIE9USEVSCiAgICAgIExJQUJJTElUWSwgV0hFVEhFUiBJTiBBTiBBQ1RJT04gT0YgQ09OVFJBQ1QsIFRPUlQgT1IgT1RIRVJXSVNFLCBBUklTSU5HIEZST00sCiAgICAgIE9VVCBPRiBPUiBJTiBDT05ORUNUSU9OIFdJVEggVEhFIFNPRlRXQVJFIE9SIFRIRSBVU0UgT1IgT1RIRVIgREVBTElOR1MgSU4KICAgICAgVEhFIFNPRlRXQVJFLjwvcD4KICA8L2FydGljbGU+CiAgPGZvb3Rlcj4KICA8cD48YSB0YXJnZXQ9Il9ibGFuayIgcmVsPSJub29wZW5lciIgaHJlZj0iaHR0cHM6Ly9naXRodWIuY29tL3JlbXkvbWl0LWxpY2Vuc2UiPkZvcmsgdGhpcyBwcm9qZWN0IHRvIGNyZWF0ZSB5b3VyIG93bgogICAgICBNSVQgbGljZW5zZSB0aGF0IHlvdSBjYW4gYWx3YXlzIGxpbmsgdG8uPC9hPjwvcD4KPC9mb290ZXI+Cgo8L2JvZHk+Cgo8L2h0bWw+Cg==");
  }

  public static HttpResponse environmentNode3() throws Exception {
    HttpResponse mockObject = mock(HttpResponse.class);
    doReturn(environmentNode4()).when(mockObject).body();
    return mockObject;
  }

  public static Answer<HttpResponse> getAnswer0() {
    return (InvocationOnMock invocation) -> {
      HttpRequest param0 = invocation.getArgument(0);
      HttpResponse.BodyHandler param1 = invocation.getArgument(1);
      param0.method();
      // return node failed
      // Cannot find proper class to mock class java.net.URI
      param0.uri();
      // return node failed
      // Cannot find proper class to mock class java.util.Optional
      param0.timeout();
      // return node failed
      // Cannot find proper class to mock class java.net.http.HttpHeaders
      param0.headers();
      param0.expectContinue();
      // return node failed
      // Cannot find proper class to mock class java.util.Optional
      param0.bodyPublisher();
      // return node failed
      // Cannot find proper class to mock class java.util.Optional
      param0.version();
      HttpResponse.ResponseInfo envParam0 = environmentNode1();
      // return node failed
      // Cannot mock class class jdk.internal.net.http.ResponseSubscribers$ByteArraySubscriber
      param1.apply(envParam0);
      return environmentNode3();
    } ;
  }

  public static HttpClient environmentNode0() throws Exception {
    HttpClient mockObject = mock(HttpClient.class);
    doAnswer(getAnswer0()).when(mockObject).send(any(), any());
    return mockObject;
  }

  public static HttpClient create() throws Exception {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

