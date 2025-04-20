/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SerdeStrategy {
    private final Map<Class<?>, ReReSerde> serdeCache;
    private final Set<Class<?>> noSerde;
    private final List<ReReSerde> serializers;

    public SerdeStrategy(List<Class<? extends ReReSerde>> serializers) {
        this.serdeCache = new HashMap<>();
        this.noSerde = new HashSet<>();
        this.serializers = new ArrayList<>();
        for (Class<? extends ReReSerde> serializerClass : serializers) {
            try {
                ReReSerde reReSerde = serializerClass.getConstructor().newInstance();
                this.serializers.add(reReSerde);
            } catch (Exception e) {
                throw new RuntimeException("Unable to initialize serializer.", e);
            }
        }
    }

    public ReReSerde getSerializer(Class<?> clazz) {
        return serdeCache.get(clazz);
    }

    public boolean shouldSerialize(Class<?> clazz) {
        if (serdeCache.containsKey(clazz)) {
            return true;
        } else if (noSerde.contains(clazz)) {
            return false;
        } else {
            for (ReReSerde serializer : serializers) {
                if (serializer.accept(clazz)) {
                    serdeCache.put(clazz, serializer);
                    return true;
                }
            }
        }
        noSerde.add(clazz);
        return false;
    }
}
