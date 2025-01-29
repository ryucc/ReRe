package org.ingko.api;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

public class Parrot {

    EnvironmentObjectListener listener;

    private Parrot() {
        listener = new EnvironmentObjectListener();
    }


    public static Parrot newSession() {
        return new Parrot();
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        return listener.createRoot(original, targetClass);
    }

    public String createMockito(String packageName, String methodName, String fileName) {
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(packageName, methodName, fileName);
        return mockitoSynthesizer.generateMockito(listener.getRoot());
    }


}
