package org.katie.orange.core.listener.exceptions;

public class SubclassingException extends InitializationException {
    public SubclassingException(String msg) {
        super(msg);
    }

    public SubclassingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
