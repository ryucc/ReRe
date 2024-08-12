package org.parrot.core.synthesizer;

import org.parrot.core.data.objects.Node;

public interface NamingStrategy {
    String getUniqueMockName(Node node);
}
