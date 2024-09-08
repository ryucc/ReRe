package org.ingko.core.serde.exceptions;

public class SerializationException extends Exception {
    public SerializationException(Throwable cause) {
        super(cause);
    }
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
