/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.interceptor.EnvironmentObjectListener;
import org.rere.core.listener.spies.ObjectSpy;
import org.rere.core.replay.InOrderReplayNode;
import org.rere.core.replay.ReplayObjectListener;
import org.rere.core.replay.unwrap.GraphRootUnwrapper;
import org.rere.core.replay.unwrap.LeafNodeUnwrapper;
import org.rere.core.replay.unwrap.PrimitiveUnwrapper;
import org.rere.core.replay.unwrap.SerializedUnwrapper;
import org.rere.core.replay.unwrap.SingleNodeUnwrapper;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.mockito.MockitoSingleNodeWrapper;

import java.util.ArrayList;

public class ReRe {
    private final ReReIntermediateData reReIntermediateData;
    EnvironmentObjectWrapper environmentObjectWrapper;

    public ReRe() {
        this(new ReReSettings());
    }


    /**
     * Initiate an instance of ReRe.
     */
    public ReRe(ReReSettings reReSettings) {
        reReIntermediateData = new ReReIntermediateData(new ArrayList<>());
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener(reReSettings);
        EnvironmentNodeManager environmentNodeManager = new EnvironmentNodeManager(environmentObjectListener, reReSettings);
        this.environmentObjectWrapper = new EnvironmentObjectWrapper(environmentNodeManager);
        environmentObjectListener.setEnvironmentObjectWrapper(environmentObjectWrapper);
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
     * @param methodName  The method name for generating the mock object. e.g. createMock
     * @param className   The class name for the generated mockito code. e.g. ClassNameMockCreator
     * @return The generated mockito code in plaintext
     */
    public String exportMockito(String packageName, String methodName, String className) {
        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer(packageName, className);
        return mockitoSynthesizer.generateMockito(reReIntermediateData.roots().get(0), methodName);
    }

    public <T> T createReplayMock(EnvironmentNode node, Class<T> targetClass) {

        ReplayObjectListener replayObjectListener = new ReplayObjectListener();
        MockitoSingleNodeWrapper<InOrderReplayNode> wrapper = new MockitoSingleNodeWrapper<>(replayObjectListener,
                ObjectSpy.class);
        SingleNodeUnwrapper singleNodeUnwrapper = new SingleNodeUnwrapper();
        singleNodeUnwrapper.registerChild(new PrimitiveUnwrapper());
        singleNodeUnwrapper.registerChild(new SerializedUnwrapper());
        singleNodeUnwrapper.registerChild(new LeafNodeUnwrapper(wrapper));
        GraphRootUnwrapper graphRootUnwrapper = new GraphRootUnwrapper(singleNodeUnwrapper);
        replayObjectListener.setGraphRootUnwrapper(graphRootUnwrapper);


        return targetClass.cast(graphRootUnwrapper.unwrap(node));
    }
}
