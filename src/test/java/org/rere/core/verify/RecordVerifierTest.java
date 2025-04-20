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

class RecordVerifierTest {
    @Test
    public void test() {
        RecordVerifier recordVerifier = new RecordVerifier();

        recordVerifier.verify(10);
        recordVerifier.verify("Hello");
        recordVerifier.verify('c');
        recordVerifier.verify(true);

        ReReVerifyData verifyData = recordVerifier.getVerifyData();

        assertThat(verifyData.getResultList()).containsExactlyElementsOf(List.of(10, "Hello", 'c', true));

    }

    @Test
    public void testResolver() {
        RecordVerifier recordVerifier = new RecordVerifier();
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