package org.rere.api;

import org.rere.core.data.objects.EnvironmentNode;

import java.util.List;

public record ReReIntermediateData(List<EnvironmentNode> roots) {
}
