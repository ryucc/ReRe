/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.interceptor.EnvironmentObjectListener;
import org.rere.core.listener.wrap.EnvironmentObjectWrapper;
import org.rere.core.listener.wrap.ReReWrapResult;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.ArrayList;

public class ReRe {
    private final ReReIntermediateData reReIntermediateData;
    EnvironmentObjectWrapper environmentObjectWrapper;
    private ReRe() {
        reReIntermediateData = new ReReIntermediateData(new ArrayList<>());
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        EnvironmentNodeManager environmentNodeManager = new EnvironmentNodeManager(environmentObjectListener);
        this.environmentObjectWrapper = new EnvironmentObjectWrapper(environmentNodeManager);
        environmentObjectListener.setEnvironmentObjectWrapper(environmentObjectWrapper);
    }

    public static ReRe newSession() {
        return new ReRe();
    }

    public ReReIntermediateData getReReIntermediateData() {
        return reReIntermediateData;
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ReReWrapResult<T, EnvironmentNode> result = environmentObjectWrapper.createRoot(original, targetClass);
        reReIntermediateData.roots().add(result.node());
        return result.wrapped();
    }

    public String createMockito(String packageName, String methodName, String fileName) {
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(packageName, methodName, fileName);
        return mockitoSynthesizer.generateMockito(reReIntermediateData.roots().get(0));
    }


}
