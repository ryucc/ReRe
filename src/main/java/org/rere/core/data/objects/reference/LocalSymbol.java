/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects.reference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalSymbol implements Serializable {
    private final Source source;
    private final int index;

    public List<Member> getAccessPath() {
        return accessPath;
    }

    // TODO: hash needs to include access path
    @Override
    public int hashCode() {
        return Objects.hash(index, source);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if(!(other instanceof LocalSymbol)) {
            return false;
        }
        LocalSymbol otherSymbol = (LocalSymbol) other;
        return otherSymbol.getIndex() == this.index &&
                otherSymbol.getSource() == this.source;
    }

    public static LocalSymbol throwValue(int index) {
        return new LocalSymbol(Source.THROW, index);
    }
    public static LocalSymbol local(int index) {
        return new LocalSymbol(Source.LOCAL_ENV, index);
    }
    public static LocalSymbol returnValue(int index) {
        return new LocalSymbol(Source.RETURN_VALUE, index);
    }
    public static LocalSymbol param(int index) {
        return new LocalSymbol(Source.PARAMETER, index);
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
        if(source == Source.LOCAL_ENV) {
            return "local" + index;
        } else if(source == Source.RETURN_VALUE) {
            return "return" + index;
        } else if(source == Source.PARAMETER) {
            return "param" + index;
        } else if(source == Source.THROW) {
            return "throw" + index;
        }
        return "unknown";
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
