package org.ingko.core.replay;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.wrap.bytebuddy.ClassRepo;
import org.ingko.core.listener.ObjectInitializer;
import org.ingko.core.listener.exceptions.InitializationException;
import org.ingko.core.listener.exceptions.SubclassingException;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.replay.spies.InOrderReplaySpy;

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
            ((InOrderReplaySpy) mocked).setParrotNodePointer(node);
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
        // EnvironmentNode environmentNode = ParrotFieldAccessors.getNode(self);
        // InOrderReplayState state = null;
        // EnvironmentMethodCall environmentMethodCall = findNextCall(environmentNode, state);
        // return getMock(environmentMethodCall.getDest());
        return null;
    }
}
