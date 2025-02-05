/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.exceptions;

public class SubclassingException extends InitializationException {
    public SubclassingException(String msg) {
        super(msg);
    }

    public SubclassingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
