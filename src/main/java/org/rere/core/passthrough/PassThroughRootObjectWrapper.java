/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.passthrough;

import org.rere.api.ReReplayData;
import org.rere.core.wrap.ReReRootObjectWrapper;

import java.util.ArrayList;

public class PassThroughRootObjectWrapper implements ReReRootObjectWrapper {
    @Override
    public <T> T wrapRootObject(Object original, Class<T> targetClass) {
        return targetClass.cast(original);
    }
    @Override
    public ReReplayData getReplayData() {
        return new ReReplayData(new ArrayList<>());
    }
}
