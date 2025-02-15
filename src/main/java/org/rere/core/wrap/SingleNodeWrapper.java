/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

public interface SingleNodeWrapper<NODE> {
    Object initiateSpied(Object returnValue, NODE node);
}
