/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.verify;

public class VerificationFailedException extends RuntimeException {
    public VerificationFailedException() {
    }
    public VerificationFailedException(String cause) {
        super(cause);
    }
}
