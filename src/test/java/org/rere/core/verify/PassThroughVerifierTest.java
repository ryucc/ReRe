/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.verify;


import org.junit.jupiter.api.Test;
import org.rere.api.ReReVerifyData;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class PassThroughVerifierTest {
    @Test
    public void test() {
        PassThroughVerifier recordVerifier = new PassThroughVerifier();

        recordVerifier.verify(10);
        recordVerifier.verify("Hello");
        recordVerifier.verify('c');
        recordVerifier.verify(true);

        ReReVerifyData verifyData = recordVerifier.getVerifyData();

        assertThat(verifyData.getResultList()).isEmpty();

    }

    @Test
    public void testResolver() {
        PassThroughVerifier recordVerifier = new PassThroughVerifier();
        Resolver resolver = new Resolver();
        recordVerifier.verify(10, resolver);
        recordVerifier.verify("Hello", resolver);
        recordVerifier.verify('c', resolver);
        recordVerifier.verify(true, resolver);

        assertThat(resolver.isCalled()).isFalse();
    }

    private static class Resolver implements Consumer<ReReVerificationFailure> {
        boolean called;

        public Resolver() {
            called = false;
        }

        public boolean isCalled() {
            return called;
        }

        @Override
        public void accept(ReReVerificationFailure verificationFailure) {
            called = true;
        }
    }

}