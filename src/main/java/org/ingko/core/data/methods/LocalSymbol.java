package org.ingko.core.data.methods;

public class LocalSymbol {
    private final Source source;

    @Override
    public String toString() {
        return switch (source) {
            case LOCAL_ENV -> "local " + String.valueOf(index);
            case PARAMETER -> "param " + String.valueOf(index);
            case RETURN_VALUE -> "return " + String.valueOf(index);
            case THROW -> "throw " + String.valueOf(index);
            default -> "unknown";
        };
    }

    private final int index;

    public LocalSymbol(Source source, int index) {
        this.index = index;
        this.source = source;
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
