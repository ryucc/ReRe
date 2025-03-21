/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.verify;

import org.rere.api.ReReVerifyData;
import org.rere.api.ReReplayData;

import java.util.function.Consumer;

public class ReplayVerifier implements ReReVerifier {
    private final ReReVerifyData verifyData;
    private int index;

    public ReplayVerifier(ReReVerifyData verifyData) {
        this.verifyData = verifyData;
        index = 0;
    }

    @Override
    public ReReVerifyData getVerifyData() {
        return verifyData;
    }

    private void verifyAll(Object input) {
        if (index >= verifyData.getResultList().size()) {
            String errorMsg = String.format("%d-th verification called, but only %d recorded.",
                    index,
                    verifyData.getResultList().size());
            throw new VerificationFailedException(errorMsg);
        }
        if (input == verifyData.getResultList().get(index)) {
            index++;
        } else {
            String errorMsg = String.format("Recorded value was %s, but actual value was %s",
                    verifyData.getResultList().get(index),
                    input);
            index++;
            throw new VerificationFailedException(errorMsg);
        }
    }

    @Override
    public void verify(Number input) {
        verifyAll(input);
    }

    public void verifyAll(Object input, Consumer<ReReVerificationFailure> failureResolve) {
        if (index >= verifyData.getResultList().size()) {
            String errorMsg = String.format("%d-th verification called, but only %d recorded.",
                    index,
                    verifyData.getResultList().size());
            throw new VerificationFailedException(errorMsg);
        }
        if (input == verifyData.getResultList().get(index)) {
            index++;
        } else {
            ReReVerificationFailure verificationFailure = new ReReVerificationFailure(verifyData.getResultList()
                    .get(index), input);
            failureResolve.accept(verificationFailure);
        }
    }

    @Override
    public void verify(Number input, Consumer<ReReVerificationFailure> failureResolve) {
        verifyAll(input, failureResolve);
    }

    @Override
    public void verify(Character input) {
        verifyAll(input);
    }

    @Override
    public void verify(Character input, Consumer<ReReVerificationFailure> failureResolve) {

        verifyAll(input, failureResolve);
    }

    @Override
    public void verify(Boolean input) {
        verifyAll(input);

    }

    @Override
    public void verify(Boolean input, Consumer<ReReVerificationFailure> failureResolve) {

        verifyAll(input, failureResolve);
    }

    @Override
    public void verify(String input) {
        verifyAll(input);

    }

    @Override
    public void verify(String input, Consumer<ReReVerificationFailure> failureResolve) {
        verifyAll(input, failureResolve);

    }

}
