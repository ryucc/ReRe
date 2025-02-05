/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testUtils;

import org.rere.core.data.objects.EnvironmentNode;

public class SimpleUUIDNaming implements NamingStrategy {

    @Override
    public String getUniqueMockName(EnvironmentNode environmentNode) {
        String[] arr = environmentNode.getRuntimeClass().getName().split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        if (!environmentNode.isTerminal()) {
            return "mock" + last + "_" + environmentNode.getUuid().toString().substring(0, 4);
        }
        String lastlast = last.substring(0, 1).toLowerCase() + last.substring(1);
        return lastlast + "_" + environmentNode.getUuid().toString().substring(0, 4);
    }
}
