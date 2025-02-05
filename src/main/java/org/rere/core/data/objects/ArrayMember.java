/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects;

public class ArrayMember implements Member{
    public ArrayMember(int index) {
        this.index = index;
    }

    private final int index;

    public String getPath() {
        return String.format("[%d]", index);
    }
}
