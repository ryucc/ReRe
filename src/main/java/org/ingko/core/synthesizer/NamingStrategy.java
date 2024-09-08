package org.ingko.core.synthesizer;

import org.ingko.core.data.objects.Node;

public interface NamingStrategy {
    String getUniqueMockName(Node node);
}
