/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.exceptions;

public class InitializationException extends Exception{
    public InitializationException(String msg) {
        super(msg);
    }
    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
