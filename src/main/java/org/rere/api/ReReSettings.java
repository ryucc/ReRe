/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import org.rere.core.serde.ReReSerde;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ReReSettings {
    final Set<Class<?>> skipMethodTracingClasses;
    final Map<Class<?>, Class<? extends ReReSerde<?>>> customSerde;
    final boolean parameterModding;
    final ReReMode reReMode;
    final Optional<ReReData> reReData;

    public Optional<ReReData> getReReData() {
        return reReData;
    }


    public ReReSettings(ReReMode reReMode,
                        Set<Class<?>> skipMethodTracingClasses,
                        Map<Class<?>, Class<? extends ReReSerde<?>>> customSerde,
                        boolean parameterModding, Optional<ReReData> reReData) {
        this.skipMethodTracingClasses = skipMethodTracingClasses;
        this.customSerde = customSerde;
        this.parameterModding = parameterModding;
        this.reReMode = reReMode;
        this.reReData = reReData;
    }

    public ReReSettings() {
        reReMode = ReReMode.RECORD;
        parameterModding = false;
        skipMethodTracingClasses = new HashSet<>();
        customSerde = new HashMap<>();
        reReData = Optional.empty();
    }

    public ReReMode getReReMode() {
        return reReMode;
    }

    public Map<Class<?>, Class<? extends ReReSerde<?>>> getCustomSerde() {
        return customSerde;
    }

    public Set<Class<?>> skipMethodTracingClasses() {
        return skipMethodTracingClasses;
    }

    public boolean parameterModding() {
        return parameterModding;
    }

    /**
     * Skip tracing the method calls invoked on parameters.<p></p>
     * ReRe does not know if the method calls invoked on parameters will change the parameter state or not.
     * By default, ReRe will trace all those method calls for replay correctness. If the user is sure that the
     * parameters are immutable, or the method calls on the parameters are read-only, this no parameter modding option
     * can be enabled for cleaner output code.
     *
     * @param parameterModding new boolean value.
     * @return A copy of ReReSettings with new parameterModding value.
     */
    public ReReSettings withParameterModding(boolean parameterModding) {
        return new ReReSettings(reReMode, skipMethodTracingClasses, customSerde, parameterModding, reReData);
    }

    /**
     * User implementation of serialization. This may be useful when a class is final, ReRe is not able to spy on final classes. But if the class is serializable, replay is still possible.
     * TODO not implemented yet.
     *
     * @param clazz      The target class to serialize.
     * @param serializer User's custom implementation of ReReSerde for class T.
     */
    public ReReSettings registerSerializer(Class<?> clazz, Class<? extends ReReSerde<?>> serializer) {
        Set<Class<?>> set = new HashSet<>(skipMethodTracingClasses);
        Map<Class<?>, Class<? extends ReReSerde<?>>> serdeCopy = new HashMap<>(customSerde);
        serdeCopy.put(clazz, serializer);
        return new ReReSettings(reReMode, set, serdeCopy, parameterModding, reReData);
    }

    /**
     * Merge with a new set of settings. The input settings are chosen if duplicate.
     *
     * @param otherSettings new set of settings to update with.
     * @return Merged settings.
     */
    public ReReSettings merge(ReReSettings otherSettings) {
        Map<Class<?>, Class<? extends ReReSerde<?>>> copySerde = new HashMap<>(customSerde);
        copySerde.putAll(otherSettings.getCustomSerde());
        Set<Class<?>> skipCopy = new HashSet<>(skipMethodTracingClasses);
        skipCopy.addAll(otherSettings.skipMethodTracingClasses());
        return new ReReSettings(reReMode, skipCopy, copySerde, otherSettings.parameterModding(), reReData);
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
        return new ReReSettings(reReMode, set, customSerde, parameterModding, reReData);
    }

    public ReReSettings withReReMode(ReReMode reReMode) {
        return new ReReSettings(reReMode, skipMethodTracingClasses, customSerde, parameterModding, reReData);
    }
    public ReReSettings withReReplayData(ReReData reReData) {
        return new ReReSettings(reReMode, skipMethodTracingClasses, customSerde, parameterModding, Optional.of(reReData));
    }
}
