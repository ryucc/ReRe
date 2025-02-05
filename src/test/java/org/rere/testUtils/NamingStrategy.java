/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testUtils;

import org.rere.core.data.objects.EnvironmentNode;

public interface NamingStrategy {
    String getUniqueMockName(EnvironmentNode environmentNode);
}
