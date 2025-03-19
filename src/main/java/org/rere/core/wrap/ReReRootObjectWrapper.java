/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

import org.rere.api.ReReplayData;

public interface ReReRootObjectWrapper {
    <T> T wrapRootObject(Object original, Class<T> targetClass);
    ReReplayData getReplayData();
}
