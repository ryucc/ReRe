package org.ingko.core.data.objects;

public class RecordMember implements Member{
    private final String fieldName;
    public RecordMember(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPath() {
        return String.format(".%s()",fieldName);
    }
}
