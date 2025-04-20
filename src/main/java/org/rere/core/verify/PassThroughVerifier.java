/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.verify;

import org.rere.api.ReReVerifyData;

import java.util.function.Consumer;

public class PassThroughVerifier implements ReReVerifier{
    @Override
    public ReReVerifyData getVerifyData() {
        return new ReReVerifyData();
    }

    @Override
    public void verify(Number input) {

    }

    @Override
    public void verify(Number input, Consumer<ReReVerificationFailure> failureResolve) {

    }

    @Override
    public void verify(Character input) {

    }

    @Override
    public void verify(Character input, Consumer<ReReVerificationFailure> failureResolve) {

    }

    @Override
    public void verify(Boolean input) {

    }

    @Override
    public void verify(Boolean input, Consumer<ReReVerificationFailure> failureResolve) {

    }

    @Override
    public void verify(String input) {

    }

    @Override
    public void verify(String input, Consumer<ReReVerificationFailure> failureResolve) {

    }
}
