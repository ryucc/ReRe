/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;


import org.junit.jupiter.api.Test;
import org.rere.core.data.objects.reference.ArrayMember;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.data.objects.reference.OptionalMember;
import org.rere.core.data.objects.reference.RecordMember;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LocalVariableAccessorTest {
    @Test
    public void testArray() {
        Integer[][] array = {{0,1,2}, {3,4,5}};

        LocalSymbol localSymbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0);
        localSymbol.appendPath(new ArrayMember(1));
        localSymbol.appendPath(new ArrayMember(2));
        Object member = new LocalVariableAccessor().getChild(array, localSymbol);
        assertThat(member).isEqualTo(5);
    }
    @Test
    public void testOptional() {
        Optional<Optional<Integer>> oInt = Optional.of(Optional.of(10));

        LocalSymbol localSymbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0);
        localSymbol.appendPath(new OptionalMember());
        localSymbol.appendPath(new OptionalMember());
        Object member = new LocalVariableAccessor().getChild(oInt, localSymbol);
        assertThat(member).isEqualTo(10);
    }
    @Test
    public void testRecord() {
        MyRecord myRecord = new MyRecord(123, "someString");

        LocalSymbol intSymbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0);
        intSymbol.appendPath(new RecordMember("integer"));
        Object intMember = new LocalVariableAccessor().getChild(myRecord, intSymbol);
        assertThat(intMember).isEqualTo(123);

        LocalSymbol stringSymbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0);
        stringSymbol.appendPath(new RecordMember("string"));
        Object stringMember = new LocalVariableAccessor().getChild(myRecord, stringSymbol);
        assertThat(stringMember).isEqualTo("someString");
    }

    record MyRecord (int integer, String string){
    }
}