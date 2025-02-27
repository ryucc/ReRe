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
package org.rere.examples.httpclients.apache;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.rere.core.serde.PrimitiveSerde;

public class ApacheHttpClientExampleExpected {
  private static final PrimitiveSerde defaultSerde = new PrimitiveSerde();

  public static InputStream environmentNode3() throws Exception {
    InputStream mockObject = mock(InputStream.class);
    int local0 = 368;
    int local1 = 1001;
    int local2 = 1033;
    int local3 = 484;
    int local4 = -1;
    doReturn(local0).doReturn(local1).doReturn(local2).doReturn(local3).doReturn(local4).when(mockObject).read(any(), anyInt(), anyInt());
    int local5 = 1;
    int local6 = 1;
    int local7 = 1;
    int local8 = 0;
    doReturn(local5).doReturn(local6).doReturn(local7).doReturn(local8).when(mockObject).available();
    doNothing().when(mockObject).close();
    return mockObject;
  }

  public static String environmentNode7() throws Exception {
    // text/html
    return (String) defaultSerde.deserialize("rO0ABXQACXRleHQvaHRtbA==");
  }

  public static String environmentNode10() throws Exception {
    // utf-8
    return (String) defaultSerde.deserialize("rO0ABXQABXV0Zi04");
  }

  public static String environmentNode11() throws Exception {
    // charset
    return (String) defaultSerde.deserialize("rO0ABXQAB2NoYXJzZXQ=");
  }

  public static BasicNameValuePair environmentNode9() throws Exception {
    BasicNameValuePair mockObject = mock(BasicNameValuePair.class);
    String local0 = environmentNode10();
    doReturn(local0).when(mockObject).getValue();
    String local1 = environmentNode11();
    doReturn(local1).when(mockObject).getName();
    return mockObject;
  }

  public static NameValuePair[] environmentNode8() throws Exception {
    NameValuePair[] object0;
    BasicNameValuePair object1;
    object0 = new NameValuePair[1];
    object1 = environmentNode9();
    object0[0] = object1;
    return object0;
  }

  public static BasicHeaderElement environmentNode6() throws Exception {
    BasicHeaderElement mockObject = mock(BasicHeaderElement.class);
    String local0 = environmentNode7();
    doReturn(local0).when(mockObject).getName();
    NameValuePair[] local1 = environmentNode8();
    doReturn(local1).when(mockObject).getParameters();
    return mockObject;
  }

  public static HeaderElement[] environmentNode5() throws Exception {
    HeaderElement[] object0;
    BasicHeaderElement object1;
    object0 = new HeaderElement[1];
    object1 = environmentNode6();
    object0[0] = object1;
    return object0;
  }

  public static BufferedHeader environmentNode4() throws Exception {
    BufferedHeader mockObject = mock(BufferedHeader.class);
    HeaderElement[] local0 = environmentNode5();
    doReturn(local0).when(mockObject).getElements();
    return mockObject;
  }

  public static DecompressingEntity environmentNode2() throws Exception {
    DecompressingEntity mockObject = mock(DecompressingEntity.class);
    InputStream local0 = environmentNode3();
    doReturn(local0).when(mockObject).getContent();
    BufferedHeader local1 = environmentNode4();
    doReturn(local1).when(mockObject).getContentType();
    long local2 = -1;
    long local3 = -1;
    doReturn(local2).doReturn(local3).when(mockObject).getContentLength();
    return mockObject;
  }

  public static CloseableHttpResponse environmentNode1() throws Exception {
    CloseableHttpResponse mockObject = mock(CloseableHttpResponse.class);
    DecompressingEntity local0 = environmentNode2();
    doReturn(local0).when(mockObject).getEntity();
    doNothing().when(mockObject).close();
    return mockObject;
  }

  public static CloseableHttpClient environmentNode0() throws Exception {
    CloseableHttpClient mockObject = mock(CloseableHttpClient.class);
    CloseableHttpResponse local0 = environmentNode1();
    doReturn(local0).when(mockObject).execute(any());
    return mockObject;
  }

  public static CloseableHttpClient create() {
    try {
      return environmentNode0();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

