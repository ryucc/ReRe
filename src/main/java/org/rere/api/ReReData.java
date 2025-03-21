/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

public class ReReData {
    private final ReReplayData reReplayData;
    private final ReReVerifyData reReVerifyData;

    public ReReData(ReReplayData reReplayData, ReReVerifyData reReVerifyData) {
        this.reReplayData = reReplayData;
        this.reReVerifyData = reReVerifyData;
    }

    public ReReplayData getReReplayData() {
        return reReplayData;
    }

    public ReReVerifyData getReReVerifyData() {
        return reReVerifyData;
    }
}
