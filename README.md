# Java's Little Parrot

## Introduction

Java's little parrot is a tool to record and replay (most) Java objects.


## Basic Example

Create Mockito java code by replaying executions.

Let's say you have a dependency with random behavior

```java
import java.util.Random;

public class Dice {
    public Dice(){}

    public int roll() {
        return new Random().nextInt(6) + 1;
    }
}
```

It would be nice to have some way to generate the following code by recording a real object.

```java
public class MockDiceCreator {
  public Dice create() {
    Dice mockDice = Mockito.mock(Dice.class);
    doReturn(2)
            .doReturn(3)
            .doReturn(3)
            .when(mockDice)
            .roll();
    return mockDice;
  }
}
```
Good news!
This package enables you to do so!

Just modify your running code in 2 steps
1. Wrapping the target object with a `Listener`
2. Generating the code with a `CodeSynthesizer`

That's it!

Example:

```java
import listener.core.org.parrot.Listener;
import synthesizer.core.org.parrot.CodeSynthesizer;

public class Main {
    public static void main(String[] args) {

        Dice dice = new Dice();

        Listener listener = new Listener();
        Dice wrappedDice = listener.wrap(dice);
        for (int i = 1; i <= 5; i++) {
            wrappedDice.roll();
        }
        CodeSynthesizer synth = new CodeSynthesizer("org.katie.orange.examples", "create");
        System.out.println(synth.generateMockito(listener));
    }
}
```


## Recursive Mocking

Orange doesn't only return primitives. Orange mocks all the objects recursively.

For example, having an `HttpClient`, that returns a `HttpResponse`, that returns a `String` body.

```java

import java.net.http.HttpResponse;

public class Main2 {
    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        HttpResponse response = client.get();
        String body = response.getBody();
        System.out.println(body);
    }
}
```
Orange can generate the following code by listening to HttpClient



```java
public class MockHttpClientCreator {
  public static HttpClient create() {
    String string_d510 = "Hello World!";
    HttpResponse mockHttpResponse_047e = Mockito.mock(HttpResponse.class);
    doReturn(string_d510).when(mockHttpResponse_047e).getBody();
    HttpClient mockHttpClient_c11e = Mockito.mock(HttpClient.class);
    doReturn(mockHttpResponse_047e).when(mockHttpClient_c11e).get();
    return mockHttpClient_c11e;
  }
}
```

As long as your program ends, this tree is will be finite, also the leaf nodes return void or a primitive data type.

This means that Orange can mock almost any java object! (As long as there are no final classes involved)

The other limitation is Orange only records behavior that was executed at runtime.

## Recording throws

See test/java/examples/ThrowExample.java

## Arrays and Records

Arrays and Records are final by the Java's language design. But some of their properties

## Recording methods with side effects

Coming soon

## Known Issues
hashCode() and isEqual() might break, because we are subclassing.


Array + Record loops



TODO:
array support
loop detection
custom serialization
unit tests
Override final public non-static fields for all classes
BUG: private classes with no constructors declared will error
