/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay.unwrap;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.listener.utils.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphRootUnwrapper implements ReplayUnwrapper {
    /*
     * Algorithm:
     * 1. Topological sort, build child count
     * 2. Initialize nodes by reverse topological order
     * 3. Fill in arrays
     * @param node
     * @return
     */
    private final SingleNodeUnwrapper singleNodeUnwrapper;

    public GraphRootUnwrapper(SingleNodeUnwrapper singleNodeUnwrapper) {
        this.singleNodeUnwrapper = singleNodeUnwrapper;
    }

    private SortResult topologicalSort(EnvironmentNode node) {
        Queue<EnvironmentNode> front = new ArrayDeque<>();
        front.add(node);

        Queue<EnvironmentNode> readyNodes = new ArrayDeque<>();
        Map<EnvironmentNode, List<EnvironmentNode>> parents = new HashMap<>();
        Map<EnvironmentNode, Integer> childCount = new HashMap<>();
        parents.put(node, new ArrayList<>());

        while (!front.isEmpty()) {
            EnvironmentNode cur = front.poll();
            if (cur.getRuntimeClass().equals(Optional.class)) {
                if (!cur.getDirectChildren().isEmpty()) {
                    EnvironmentNode child = cur.getDirectChildren().get(0);
                    if (!parents.containsKey(child)) {
                        parents.put(child, new ArrayList<>());
                        front.add(child);
                    }
                    parents.get(child).add(cur);
                    childCount.put(cur, 1);
                } else {
                    readyNodes.add(cur);
                }
            } else if (ClassUtils.isRecord(cur.getRuntimeClass())) {
                if (!cur.getDirectChildren().isEmpty()) {
                    for (EnvironmentNode child : cur.getDirectChildren()) {
                        if (!parents.containsKey(child)) {
                            parents.put(child, new ArrayList<>());
                            front.add(child);
                        }
                        parents.get(child).add(cur);
                    }
                    childCount.put(cur, cur.getDirectChildren().size());
                } else {
                    readyNodes.add(cur);
                }
            } else if (cur.getRuntimeClass().isArray()) {
                if (!cur.getDirectChildren().isEmpty()) {
                    for (EnvironmentNode child : cur.getDirectChildren()) {
                        if (!parents.containsKey(child)) {
                            parents.put(child, new ArrayList<>());
                            front.add(child);
                        }
                    }
                    childCount.put(cur, cur.getDirectChildren().size());
                }
                readyNodes.add(cur);
            } else {
                readyNodes.add(cur);
            }
        }
        return new SortResult(readyNodes, parents, childCount);
    }

    private Map<EnvironmentNode, Object> firstInit(SortResult sortResult) {
        Map<EnvironmentNode, Object> initMap = new HashMap<>();
        Queue<EnvironmentNode> ready = sortResult.getReadyNodes();
        Map<EnvironmentNode, List<EnvironmentNode>> parents = sortResult.getParents();
        Map<EnvironmentNode, Integer> childCount = sortResult.getChildCount();

        while (!ready.isEmpty()) {
            EnvironmentNode cur = ready.poll();
            // Decrease reference count for all parents
            for (EnvironmentNode parent : parents.get(cur)) {
                int prevCount = childCount.get(parent);
                if (prevCount == 1) {
                    ready.add(parent);
                    childCount.put(parent, 0);
                } else {
                    childCount.put(parent, prevCount - 1);
                }
            }
            //Init object
            if (cur.getRuntimeClass().isArray()) {
                Object array = Array.newInstance(cur.getRuntimeClass().getComponentType(),
                        cur.getDirectChildren().size());
                initMap.put(cur, array);
            } else if (cur.getRuntimeClass().equals(Optional.class)) {
                if (cur.getDirectChildren().isEmpty()) {
                    Object optional = Optional.empty();
                    initMap.put(cur, optional);
                } else {
                    Object child = initMap.get(cur.getDirectChildren().get(0));
                    Object optional = Optional.of(child);
                    initMap.put(cur, optional);
                }
            } else if (ClassUtils.isRecord(cur.getRuntimeClass())) {
                try {
                    List<Object> components = cur.getDirectChildren()
                            .stream()
                            .map(initMap::get)
                            .collect(Collectors.toList());
                    Constructor<?> constructor = cur.getRuntimeClass().getConstructors()[0];
                    Object result = constructor.newInstance(components.toArray());
                    initMap.put(cur, result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Object replayObject = singleNodeUnwrapper.unwrap(cur);
                initMap.put(cur, replayObject);
            }
        }
        return initMap;
    }

    private void postAssign(EnvironmentNode root, Map<EnvironmentNode, Object> initMap) {
        Queue<EnvironmentNode> front = new ArrayDeque<>();
        Set<EnvironmentNode> explored = new HashSet<>();

        while(!front.isEmpty()) {
            EnvironmentNode cur = front.poll();
            if(cur.getRuntimeClass().isArray()) {
                Object array = initMap.get(cur);
                Object[] children = cur.getDirectChildren().stream()
                        .map(initMap::get)
                        .toArray();
                ClassUtils.shallowCopyIntoArray(children, array);
            }
            for (EnvironmentNode child: cur.getDirectChildren()) {
                if(!explored.contains(child)) {
                    explored.add(child);
                    front.add(child);
                }
            }
        }
    }

    @Override
    public Object unwrap(EnvironmentNode node) {
        SortResult result = topologicalSort(node);
        Map<EnvironmentNode, Object> initMap = firstInit(result);
        postAssign(node, initMap);
        return initMap.get(node);
    }

    @Override
    public boolean accept(EnvironmentNode node) {
        return true;
    }

    private class SortResult {
        private final Queue<EnvironmentNode> readyNodes;
        private final Map<EnvironmentNode, List<EnvironmentNode>> parents;
        private final Map<EnvironmentNode, Integer> childCount;

        public SortResult(Queue<EnvironmentNode> readyNodes,
                          Map<EnvironmentNode, List<EnvironmentNode>> parents,
                          Map<EnvironmentNode, Integer> childCount) {
            this.readyNodes = readyNodes;
            this.parents = parents;
            this.childCount = childCount;
        }

        private SortResult() {
            readyNodes = new ArrayDeque<>();
            parents = new HashMap<>();
            childCount = new HashMap<>();
        }

        public Queue<EnvironmentNode> getReadyNodes() {
            return readyNodes;
        }

        public Map<EnvironmentNode, List<EnvironmentNode>> getParents() {
            return parents;
        }

        public Map<EnvironmentNode, Integer> getChildCount() {
            return childCount;
        }
    }
}
