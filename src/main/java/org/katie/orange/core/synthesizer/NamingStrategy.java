package org.katie.orange.core.synthesizer;

import org.katie.orange.core.data.objects.Node;

public interface NamingStrategy {
    String getUniqueMockName(Node node);
}
