package org.katie.orange.core;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.methods.MethodCall;
import org.katie.orange.core.data.methods.MethodResult;
import org.katie.orange.core.data.methods.PrimitiveMethodCall;
import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.objects.Node;
import org.katie.orange.core.data.objects.PrimitiveWrapperNode;
import org.katie.orange.core.data.objects.ThrowableNode;

import java.lang.reflect.Method;
import java.util.Set;

public class RecordInterceptor2 {
    public RecordInterceptor2() {
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments, @Origin Method orignalMethod, @This Object self) throws Exception {
        Node source = (Node) self.getClass().getField("objectNode").get(self);

        try {
            System.out.println(orignalMethod.getName());
            Object original = self.getClass().getField("original").get(self);
            System.out.println("asdsadas" + original.getClass());
            Object returnValue = orignalMethod.invoke(original, allArguments);
            Node dest = new Node(returnValue.getClass());
            MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.RETURN);
            source.addEdge(edge);
            if(dest.isTerminal()) {
                return returnValue;
            }
            return new MockFactory2().createInternal(returnValue, dest);
        } catch (Exception e) {
            Node dest = new Node(e.getClass());
            MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.THROW);
            source.addEdge(edge);
            throw e;
            //throw new MockFactory2().createInternal(e, dest);
        }
    }
}
