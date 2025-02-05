/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.core.data.objects.LocalSymbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InOrderReplayState {
    private final Set<Integer> usedMethods;
    private final Map<LocalSymbol, Object> returnValues;

    public InOrderReplayState() {
        this.usedMethods = new HashSet<>();
        this.returnValues = new HashMap<>();
    }
}