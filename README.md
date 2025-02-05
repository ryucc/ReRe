# ReRe's Readme

## Introduction

ReRe is the abbreviation of **Record** and **Replay**. This started out as a project to replay general java executions, but I ran out of cash and need to find a job now. For now ReRe is released as a code synthesizer.

ReRe uses logic to synthesize code, so it's predictable right or wrong. I hope in the LLM era, this still has its place in the world. It would be my pleasure if anyone favors my hand written code over the modern LLM synthesizers.

Best of luck and I hope this project brings value to you!


## Usage

To start with ReRe, import the API first.

```java
import org.rere.api.ReRe;
```

Next create a new instance of ReRe

```java
ReRe rere = ReRe.newSession();
```

For whichever object you want to record, use the create root API. Here, we are using a dice as example.

```java
Dice dice = new Dice();
Dice rereDice = rere.createRoot(dice, Dice.class);
```

Now rereDice is a copy of dice, that records its behavior. Use the rereDice inplace of dice in your code.

For example,


```java
for (int i = 1; i <= 5; i++) {
    System.out.println("Rolled " + rereDice.roll());
}
```

After you are done recording, use the createMockito() api

```java
String code = rere.createMockito("org.rere.examples.readme", "create", "ReadmeExampleExpected");
```

ReRe will generate the following code,


```java
public class ReadmeExampleExpected {
  private static final DefaultSerde defaultSerde = new DefaultSerde();

  public static ReadmeExample.Dice environmentNode0() {
    ReadmeExample.Dice mockObject = mock(ReadmeExample.Dice.class);
    doReturn(4).doReturn(5).doReturn(2).doReturn(4).doReturn(3).when(mockObject).roll();
    return mockObject;
  }
}
```

Of course, ReRe is capable of more than just recording primitive value returns. The following effects are also recorded,

1. Exceptions thrown.
2. Modifications to method parameters.
3. Methods of the return values.

The easiest way might be to test it on your code, or look at our examples under test/java/org/rere/examples. This is so that our examples are always compiled and tested up to date. But here we will still include few examples.

## Limitations
### Final objects
### Global Variables

## Contributions

Please raise bug reports and issues first. I'm not ready to accept PRs yet.

## Known Issues
hashCode() and isEqual() might break, because we are subclassing.