/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.wrap;

public interface SingleNodeWrapper<NODE> {
    <T> T initiateSpied(T returnValue, NODE node);
}
