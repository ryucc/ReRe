package org.ingko.api;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentNodeManager;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.listener.wrap.EnvironmentObjectWrapper;
import org.ingko.core.listener.wrap.ParrotWrapResult;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.ArrayList;

public class Parrot {
    private final ParrotIntermediateData parrotIntermediateData;
    EnvironmentObjectWrapper environmentObjectWrapper;
    private Parrot() {
        parrotIntermediateData = new ParrotIntermediateData(new ArrayList<>());
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        EnvironmentNodeManager environmentNodeManager = new EnvironmentNodeManager(environmentObjectListener);
        this.environmentObjectWrapper = new EnvironmentObjectWrapper(environmentNodeManager);
        environmentObjectListener.setEnvironmentObjectWrapper(environmentObjectWrapper);
    }

    public static Parrot newSession() {
        return new Parrot();
    }

    public ParrotIntermediateData getParrotIntermediateData() {
        return parrotIntermediateData;
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ParrotWrapResult<T, EnvironmentNode> result = environmentObjectWrapper.createRoot(original, targetClass);
        parrotIntermediateData.roots().add(result.node());
        return result.wrapped();
    }

    public String createMockito(String packageName, String methodName, String fileName) {
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(packageName, methodName, fileName);
        return mockitoSynthesizer.generateMockito(parrotIntermediateData.roots().getFirst());
    }


}
