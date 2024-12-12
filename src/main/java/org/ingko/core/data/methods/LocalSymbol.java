package org.ingko.core.data.methods;

import org.ingko.core.data.objects.Member;

import java.util.ArrayList;
import java.util.List;

public class LocalSymbol {
    private final Source source;
    private final int index;

    public List<Member> getAccessPath() {
        return accessPath;
    }

    private final List<Member> accessPath;

    public void appendPath(Member member) {
        accessPath.add(member);
    }
    public LocalSymbol(Source source, int index) {
        this.source = source;
        this.index = index;
        this.accessPath = new ArrayList<>();
    }
    public LocalSymbol(Source source, int index, List<Member> accessPath) {
        this.source = source;
        this.index = index;
        this.accessPath = accessPath;
    }

    public LocalSymbol copy() {
        return new LocalSymbol(this.getSource(), this.getIndex(), new ArrayList<>(accessPath));
    }

    @Override
    public String toString() {
        return switch (source) {
            case LOCAL_ENV -> "local " + index;
            case PARAMETER -> "param " + index;
            case RETURN_VALUE -> "return " + index;
            case THROW -> "throw " + index;
            default -> "unknown";
        };
    }

    public int getIndex() {
        return index;
    }

    public Source getSource() {
        return source;
    }

    public enum Source {
        RETURN_VALUE, PARAMETER, LOCAL_ENV, THROW
    }
}
