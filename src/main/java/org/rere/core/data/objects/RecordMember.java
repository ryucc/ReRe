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
