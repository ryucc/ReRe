package org.rere.testUtils;

import org.rere.core.data.objects.EnvironmentNode;

public interface NamingStrategy {
    String getUniqueMockName(EnvironmentNode environmentNode);
}
