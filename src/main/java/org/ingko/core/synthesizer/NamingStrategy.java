package org.ingko.core.synthesizer;

import org.ingko.core.data.objects.EnvironmentNode;

public interface NamingStrategy {
    String getUniqueMockName(EnvironmentNode environmentNode);
}
