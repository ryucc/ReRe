/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.core.data.objects.reference.ArrayMember;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.data.objects.reference.Member;
import org.rere.core.data.objects.reference.OptionalMember;
import org.rere.core.data.objects.reference.RecordMember;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Optional;

public class LocalVariableAccessor {
    public Object getChild(Object root, LocalSymbol symbol) {
        Object cur = root;
        for(Member member: symbol.getAccessPath()) {
            if(member instanceof ArrayMember) {
                ArrayMember arrayMember = (ArrayMember) member;
                cur = Array.get(cur, arrayMember.getIndex());
            } else if (member instanceof OptionalMember) {
                cur = ((Optional<?>) cur).orElseThrow(() ->
                        new RuntimeException("Trying to access optional member, but optional is empty."));
            } else if(member instanceof RecordMember){
                RecordMember recordMember = (RecordMember) member;
                try {
                    Method getter = cur.getClass().getMethod(recordMember.getFieldName());
                    cur = getter.invoke(cur);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Unrecognized member type.");
            }
        }
        return cur;
    }
}
