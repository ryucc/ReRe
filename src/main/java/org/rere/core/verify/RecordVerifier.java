/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.verify;

import org.rere.api.ReReVerifyData;

import java.util.function.Consumer;

public class RecordVerifier implements ReReVerifier {
    private final ReReVerifyData verifyData;

    public RecordVerifier() {
        this.verifyData = new ReReVerifyData();
    }

    @Override
    public ReReVerifyData getVerifyData() {
        return this.verifyData;
    }

    @Override
    public void verify(Number input) {
        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(Number input, Consumer<ReReVerificationFailure> failureResolve) {
        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(Character input) {
        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(Character input, Consumer<ReReVerificationFailure> failureResolve) {

        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(Boolean input) {
        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(Boolean input, Consumer<ReReVerificationFailure> failureResolve) {

        verifyData.getResultList().add(input);
    }

    @Override
    public void verify(String input) {
        verifyData.getResultList().add(input);

    }

    @Override
    public void verify(String input, Consumer<ReReVerificationFailure> failureResolve) {
        verifyData.getResultList().add(input);

    }
}
