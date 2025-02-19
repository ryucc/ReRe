/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde.exceptions;

public class SerializationException extends Exception {
    public SerializationException(Throwable cause) {
        super(cause);
    }
    public SerializationException(String cause) {
        super(cause);
    }
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
