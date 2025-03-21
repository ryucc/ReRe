/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.verify;

public class ReReVerificationFailure {
    private final Object expectedValue;
    private final Object actualValue;

    public ReReVerificationFailure(Object expectedValue, Object actualValue) {
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    public Object getActualValue() {
        return actualValue;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

}
