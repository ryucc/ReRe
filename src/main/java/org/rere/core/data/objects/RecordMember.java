/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects;

public class RecordMember implements Member{
    private final String fieldName;
    public RecordMember(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPath() {
        return String.format(".%s()",fieldName);
    }
}
