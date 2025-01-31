package org.ingko.core.replay;

import org.ingko.core.data.objects.LocalSymbol;

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