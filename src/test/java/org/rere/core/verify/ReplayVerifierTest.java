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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplayVerifierTest {
    @Test
    public void test() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(10, "Hello", 'c', true));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);

        // assert no throw
        replayVerifier.verify(10);
        replayVerifier.verify("Hello");
        replayVerifier.verify('c');
        replayVerifier.verify(true);
    }

    @Test
    public void testResolver() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(10, "Hello", 'c', true));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);
        Resolver resolver = new Resolver();
        replayVerifier.verify(10, resolver);
        replayVerifier.verify("Hello", resolver);
        replayVerifier.verify('c', resolver);
        replayVerifier.verify(true, resolver);

        assertThat(resolver.isCalled()).isFalse();
    }

    @Test
    public void testThrow() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(20));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);

        assertThatThrownBy(() -> replayVerifier.verify(10))
                .isInstanceOf(VerificationFailedException.class);
    }
    @Test
    public void testResolveFailure() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(20));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);

        Resolver resolver = new Resolver();
        replayVerifier.verify(10, resolver);

        assertThat(resolver.getReReVerificationFailure().getActualValue()).isEqualTo(10);
        assertThat(resolver.getReReVerificationFailure().getExpectedValue()).isEqualTo(20);
        assertThat(resolver.getReReVerificationFailure().getCurVerificationIndex()).isEqualTo(0);
        assertThat(resolver.getReReVerificationFailure().getMaxVerificationIndex()).isEqualTo(1);
    }

    @Test
    public void testOutOfBounds() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(20));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);

        replayVerifier.verify(20);
        assertThatThrownBy(() -> replayVerifier.verify(10))
                .isInstanceOf(VerificationFailedException.class);
    }

    @Test
    public void testOutOfBoundsResolve() {
        ReReVerifyData reReVerifyData = new ReReVerifyData(List.of(20));
        ReplayVerifier replayVerifier = new ReplayVerifier(reReVerifyData);

        Resolver resolver = new Resolver();
        replayVerifier.verify(20, resolver);
        replayVerifier.verify(39, resolver);
        assertThat(resolver.getReReVerificationFailure().getActualValue()).isEqualTo(39);
        assertThat(resolver.getReReVerificationFailure().getCurVerificationIndex()).isEqualTo(1);
        assertThat(resolver.getReReVerificationFailure().getMaxVerificationIndex()).isEqualTo(1);
    }
    private static class Resolver implements Consumer<ReReVerificationFailure> {
        boolean called;

        public ReReVerificationFailure getReReVerificationFailure() {
            return reReVerificationFailure;
        }

        ReReVerificationFailure reReVerificationFailure;

        public Resolver() {
            called = false;
        }

        public boolean isCalled() {
            return called;
        }

        @Override
        public void accept(ReReVerificationFailure verificationFailure) {
            reReVerificationFailure = verificationFailure;
            called = true;
        }
    }

}