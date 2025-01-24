package org.ingko.testUtils;

import org.ingko.core.data.objects.EnvironmentNode;

public interface NamingStrategy {
    String getUniqueMockName(EnvironmentNode environmentNode);
}
