# 

ReRe-record is a graph building problem. The execution data is represented as a tree like structure. The replay is just reading from this tree, or translating it as mockito code.

We need to first explain what this tree looks like.

## The root of the tree
ReRe tries to record an object. The obvious root of this tree would be the target object to mock. 

```
EnvironmentNode {
   class: MyObject.class
}
```

We can synthesize the following mockito code:
```
MyObject createMock() {
    MyObject mock = Mockito.mock(MyObject.class);
    return mock;
}
```
