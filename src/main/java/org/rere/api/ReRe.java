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
import org.rere.core.serde.ReReSerde;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.ArrayList;

public class ReRe {
    private final ReReIntermediateData reReIntermediateData;
    EnvironmentObjectWrapper environmentObjectWrapper;

    /**
     * Initiate an instance of ReRe.
     */
    public ReRe() {
        reReIntermediateData = new ReReIntermediateData(new ArrayList<>());
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        EnvironmentNodeManager environmentNodeManager = new EnvironmentNodeManager(environmentObjectListener);
        this.environmentObjectWrapper = new EnvironmentObjectWrapper(environmentNodeManager);
        environmentObjectListener.setEnvironmentObjectWrapper(environmentObjectWrapper);
    }

    /**
     * User implementation of serialization. This may be useful when a class is final, ReRe is not able to spy on final classes. But if the class is serializable, replay is still possible.
     * TODO not implemented yet.
     *
     * @param clazz      The target class to serialize.
     * @param serializer User's custom implementation of ReReSerde for class T.
     * @param <T>        Generic template for the target class to serialize.
     * @param <S>        Generic template for custom implementation of ReReSerde for class T
     */
    public <T, S extends ReReSerde<T>> void registerSerializer(Class<T> clazz, S serializer) {

    }

    /**
     * Returns the record data used internally. This might be used since the code synthesizer
     * only runs on java 11+, but the recording can work on java 8+.
     *
     * @return ReReIntermediateData
     */
    public ReReIntermediateData getReReRecordData() {
        return reReIntermediateData;
    }

    /**
     * Creates a spied copy of the original object. This spied copy needs to be used
     * in place of the original object for the methods to be recorded.
     *
     * @param original    The original object that is spied on and recorded.
     * @param targetClass The class of the object.
     * @param <T>
     * @return The wrapped spy of the original object.
     */
    public <T> T createSpiedObject(Object original, Class<T> targetClass) {
        ReReWrapResult<T, EnvironmentNode> result = environmentObjectWrapper.createRoot(original, targetClass);
        reReIntermediateData.roots().add(result.node());
        return result.wrapped();
    }

    /**
     * Exports the record data as mockito code
     *
     * @param packageName The package name for the generated mockito class e.g. org.rere.api
     * @param methodName  (Not implemented yet) The method name for generating the mock object
     * @param className   The class name for the generated mockito code. e.g. ClassNameMockCreator
     * @return The generated mockito code in plaintext
     */
    public String exportMockito(String packageName, String methodName, String className) {
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(packageName, methodName, className);
        return mockitoSynthesizer.generateMockito(reReIntermediateData.roots().get(0));
    }
}
