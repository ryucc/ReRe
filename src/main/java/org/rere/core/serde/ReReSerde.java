/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde;

import org.rere.core.serde.exceptions.SerializationException;

public interface ReReSerde {
    String serialize(Object object) throws SerializationException;

    /**
     * Return null or throw runtime errors on failures.
     */
    Object deserialize(String serialization);
}
