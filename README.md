# Record-Replay Mock Creator

Create Mockito java code by replaying executions.

## Example

## TODO
Serialize to mockito
mock subroutine option
add object id


### Original Code
```java
import java.net.http.HttpResponse;

HttpClient client;
HttpResponse response = client.get(...);
/*
 More code ...
 */
```
### Modified Code to produce Mockito
```java
HttpClient client;
HttpClient recordedClient = MockFactory.mock(client, client.class);
recordedClient.start();
// Replace all occurences of client with recordedClient
HttpResponse response = recordedClient.get(...);
/*
 More code, replacing client with recordedClient...
 */
mockRecorder.end();
String mockitoCode = mockRecorder.export();

System.out.println(mockitoCode);
```