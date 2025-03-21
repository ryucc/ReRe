/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.api;

import java.util.ArrayList;
import java.util.List;

public class ReReVerifyData {

    private final List<Object> resultList;

    public ReReVerifyData() {
        this(new ArrayList<>());
    }

    public ReReVerifyData(List<Object> resultList) {
        this.resultList = resultList;
    }

    public List<Object> getResultList() {
        return resultList;
    }
}
