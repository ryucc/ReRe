/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.verify;

public class ReReVerificationFailure {

    private final FailureType failureType;
    private final Object expectedValue;
    private final Object actualValue;

    private final int maxVerificationIndex;
    private final int curVerificationIndex;

    public ReReVerificationFailure(FailureType failureType,
                                   Object expectedValue,
                                   Object actualValue,
                                   int curVerificationIndex,
                                   int maxVerificationIndex) {
        this.failureType = failureType;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
        this.curVerificationIndex = curVerificationIndex;
        this.maxVerificationIndex = maxVerificationIndex;
    }

    public int getMaxVerificationIndex() {
        return maxVerificationIndex;
    }

    public int getCurVerificationIndex() {
        return curVerificationIndex;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public Object getActualValue() {
        return actualValue;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

    public enum FailureType {
        VALUE_MISMATCH, VERIFICATION_OUT_OF_BOUNDS
    }

}
