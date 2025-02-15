/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import java.util.HashSet;
import java.util.Set;

public class ReReSettings {
    final Set<Class<?>> skipMethodTracingClasses;

    public Set<Class<?>> skipMethodTracingClasses() {
        return skipMethodTracingClasses;
    }

    public boolean noParameterModding() {
        return noParameterModding;
    }

    final boolean noParameterModding;

    public ReReSettings(Set<Class<?>> skipMethodTracingClasses, boolean noParameterModding) {
        this.skipMethodTracingClasses = skipMethodTracingClasses;
        this.noParameterModding = noParameterModding;
    }

    public ReReSettings() {
        noParameterModding = false;
        skipMethodTracingClasses = new HashSet<>();
    }

    /**
     * Skip tracing the method calls invoked on parameters.<p></p>
     * ReRe does not know if the method calls invoked on parameters will change the parameter state or not.
     * By default, ReRe will trace all those method calls for replay correctness. If the user is sure that the
     * parameters are immutable, or the method calls on the parameters are read-only, this no parameter modding option
     * can be enabled for cleaner output code.
     *
     * @param noParameterModding new boolean value.
     * @return A copy of ReReSettings with new noParameterModding value.
     */
    public ReReSettings withNoParameterModding(boolean noParameterModding) {
        return new ReReSettings(skipMethodTracingClasses, noParameterModding);
    }

    public ReReSettings addSkipClass(Class<?> skipClass) {
        Set<Class<?>> set = new HashSet<>(skipMethodTracingClasses);
        set.add(skipClass);
        return new ReReSettings(set, noParameterModding);
    }
}
