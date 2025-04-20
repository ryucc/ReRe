/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.interceptor.EnvironmentObjectListener;
import org.rere.core.passthrough.PassThroughRootObjectWrapper;
import org.rere.core.replay.ReplayRootObjectWrapper;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReRootObjectWrapper;
import org.rere.core.verify.PassThroughVerifier;
import org.rere.core.verify.ReReVerificationFailure;
import org.rere.core.verify.ReReVerifier;
import org.rere.core.verify.RecordVerifier;
import org.rere.core.verify.ReplayVerifier;

import java.util.function.Consumer;

public class ReRe {
    private final ReReRootObjectWrapper reReRootObjectWrapper;
    private final ReReVerifier reReVerifier;

    public ReRe() {
        this(new ReReSettings());
    }


    /**
     * Initiate an instance of ReRe.
     */
    public ReRe(ReReSettings reReSettings) {
        if (reReSettings.getReReMode().equals(ReReMode.RECORD)) {
            EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener(reReSettings);
            EnvironmentNodeManager environmentNodeManager = new EnvironmentNodeManager(environmentObjectListener,
                    reReSettings);
            EnvironmentObjectWrapper environmentObjectWrapper = new EnvironmentObjectWrapper(environmentNodeManager);
            environmentObjectListener.setEnvironmentObjectWrapper(environmentObjectWrapper);
            this.reReRootObjectWrapper = environmentObjectWrapper;
        } else if (reReSettings.getReReMode().equals(ReReMode.REPLAY)) {
            reReRootObjectWrapper = new ReplayRootObjectWrapper(reReSettings);
        } else {
            reReRootObjectWrapper = new PassThroughRootObjectWrapper();
        }

        if (reReSettings.getReReMode().equals(ReReMode.RECORD)) {
            reReVerifier = new RecordVerifier();
        } else if (reReSettings.getReReMode().equals(ReReMode.PASS_THROUGH)) {
            reReVerifier = new PassThroughVerifier();
        } else {
            reReVerifier = new ReplayVerifier(reReSettings.getReReData().get().getReReVerifyData());
        }
    }


    /**
     * Returns the record data used internally. This might be used since the code synthesizer
     * only runs on java 11+, but the recording can work on java 8+.
     *
     * @return ReReIntermediateData
     */
    public ReReData getReReData() {
        return new ReReData(reReRootObjectWrapper.getReplayData(), reReVerifier.getVerifyData());
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
    public <T> T createReReObject(Object original, Class<T> targetClass) {
        return reReRootObjectWrapper.wrapRootObject(original, targetClass);
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
        ReReplayData reReplayData = reReRootObjectWrapper.getReplayData();
        return mockitoSynthesizer.generateMockito(reReplayData.roots().get(0), methodName);
    }

    public void verify(Number input) {
        reReVerifier.verify(input);
    }

    void verify(Number input, Consumer<ReReVerificationFailure> failureResolve){
        reReVerifier.verify(input, failureResolve);
    }
    public void verify(Character o) {
        reReVerifier.verify(o);
    }
    void verify(Character input, Consumer<ReReVerificationFailure> failureResolve){
        reReVerifier.verify(input, failureResolve);
    }
    public void verify(Boolean o) {
        reReVerifier.verify(o);
    }
    void verify(Boolean input, Consumer<ReReVerificationFailure> failureResolve){
        reReVerifier.verify(input, failureResolve);
    }
    public void verify(String o) {
        reReVerifier.verify(o);
    }
    void verify(String input, Consumer<ReReVerificationFailure> failureResolve){
        reReVerifier.verify(input, failureResolve);
    }
}
