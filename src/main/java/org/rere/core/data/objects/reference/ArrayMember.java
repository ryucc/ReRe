/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects.reference;

public class ArrayMember implements Member {
    private final int index;

    public ArrayMember(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getPath() {
        return String.format("[%d]", index);
    }
}
