/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.verify;

import org.rere.api.ReReVerifyData;

import java.util.function.Consumer;

public interface ReReVerifier {
    /**
     * Returns the verify data
     * @return
     */
    ReReVerifyData getVerifyData();

    /**
     * Verifies the input. Input must be Serializable, and supports equals.
     *
     * On Record: Records the input into file.
     * On Replay: Compares the input to the record on file. Throws error if they are not equal.
     * @param input Serializable input.
     */
    void verify(Number input);
    void verify(Number input, Consumer<ReReVerificationFailure> failureResolve);
    void verify(Character input);
    void verify(Character input, Consumer<ReReVerificationFailure> failureResolve);
    void verify(Boolean input);
    void verify(Boolean input, Consumer<ReReVerificationFailure> failureResolve);

    void verify(String input);
    void verify(String input, Consumer<ReReVerificationFailure> failureResolve);
    // verify that the runnable does not throw
    // void verify(Runnable runnable);
}
