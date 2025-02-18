/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.serde.ReReSerde;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReReSettings {
    final Set<Class<?>> skipMethodTracingClasses;
    final Map<Class<?>, Class<? extends ReReSerde<?>>> customSerde;
    final boolean noParameterModding;
    public ReReSettings(Set<Class<?>> skipMethodTracingClasses,
                        boolean noParameterModding,
                        Map<Class<?>, Class<? extends ReReSerde<?>>> customSerde) {
        this.skipMethodTracingClasses = skipMethodTracingClasses;
        this.customSerde = customSerde;
        this.noParameterModding = noParameterModding;
    }

    public ReReSettings() {
        noParameterModding = false;
        skipMethodTracingClasses = new HashSet<>();
        customSerde = new HashMap<>();
    }

    public Map<Class<?>, Class<? extends ReReSerde<?>>> getCustomSerde() {
        return customSerde;
    }

    public Set<Class<?>> skipMethodTracingClasses() {
        return skipMethodTracingClasses;
    }

    public boolean noParameterModding() {
        return noParameterModding;
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
        return new ReReSettings(skipMethodTracingClasses, noParameterModding, customSerde);
    }

    /**
     * User implementation of serialization. This may be useful when a class is final, ReRe is not able to spy on final classes. But if the class is serializable, replay is still possible.
     * TODO not implemented yet.
     *
     * @param clazz      The target class to serialize.
     * @param serializer User's custom implementation of ReReSerde for class T.
     * @param <T>        Generic template for the target class to serialize.
     */
    public <T> ReReSettings registerSerializer(Class<T> clazz, Class<? extends ReReSerde<T>> serializer) {
        Set<Class<?>> set = new HashSet<>(skipMethodTracingClasses);
        Map<Class<?>, Class<? extends ReReSerde<?>>> serdeCopy = new HashMap<>(customSerde);
        serdeCopy.put(clazz, serializer);
        return new ReReSettings(set, noParameterModding, serdeCopy);
    }

    /**
     * Skip method tracing for the class. If skipClass only contains read-only methods, this option
     * Will skip the method tracing for all instances of skipClass, making the generated code more concise.
     * Also improving runtime performance.
     *
     * @param skipClass Class to skip tracing
     * @return Updated copy of settings.
     */
    public ReReSettings addSkipClass(Class<?> skipClass) {
        Set<Class<?>> set = new HashSet<>(skipMethodTracingClasses);
        set.add(skipClass);
        return new ReReSettings(set, noParameterModding, customSerde);
    }
}
