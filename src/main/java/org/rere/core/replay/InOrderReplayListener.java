/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.wrap.bytebuddy.ClassRepo;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.exceptions.InitializationException;
import org.rere.core.listener.exceptions.SubclassingException;
import org.rere.core.listener.utils.ObjectSpy;
import org.rere.core.replay.spies.InOrderReplaySpy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


public class InOrderReplayListener {

    private final ClassRepo classRepo;

    public InOrderReplayListener() {
        classRepo = new ClassRepo(this,
                Map.of(ObjectSpy.FIELD, ObjectSpy.TYPE, InOrderReplaySpy.FIELD, InOrderReplaySpy.TYPE),
                List.of(InOrderReplaySpy.class));
    }

    public Object getMock(EnvironmentNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(node.getRuntimeClass());
            Object mocked = ObjectInitializer.create(mockedClass);
            ((InOrderReplaySpy) mocked).setReReNodePointer(node);
            return mocked;
        } catch (SubclassingException e) {
            return null;
        } catch (InitializationException e) {
            return null;
        }
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Throwable {
        // EnvironmentNode environmentNode = ReReFieldAccessors.getNode(self);
        // InOrderReplayState state = null;
        // EnvironmentMethodCall environmentMethodCall = findNextCall(environmentNode, state);
        // return getMock(environmentMethodCall.getDest());
        return null;
    }
}
