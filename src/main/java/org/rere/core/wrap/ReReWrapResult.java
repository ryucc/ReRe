/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

public class ReReWrapResult<T, NODE> {
    public ReReWrapResult(T wrapped, NODE node) {
        this.wrapped = wrapped;
        this.node = node;
    }

    final T wrapped;
    final NODE node;

    public T wrapped() {
        return wrapped;
    }

    public NODE node() {
        return node;
    }

}
