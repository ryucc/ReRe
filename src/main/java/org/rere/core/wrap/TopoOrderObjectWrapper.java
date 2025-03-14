/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.wrap;

import org.rere.core.data.objects.ReReObjectNode;
import org.rere.core.listener.NodeManager;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.exceptions.InitializationException;
import org.rere.core.listener.utils.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

/**
 * @param <NODE>
 * @param <MANAGER>
 */
public class TopoOrderObjectWrapper<NODE extends ReReObjectNode<NODE>, MANAGER extends NodeManager<NODE>> {
    private final MANAGER nodeManager;

    public TopoOrderObjectWrapper(MANAGER nodeManager) {
        this.nodeManager = nodeManager;
    }


    private List<Object> getRecordFields(Object cur) throws InitializationException {
        // Using java 5 syntax to support records
        Field[] recordComponents = cur.getClass().getDeclaredFields();
        List<Object> fields = new ArrayList<>();

        try {
            for (Field field : recordComponents) {

                field.setAccessible(true);
                Object child = field.get(cur);
                fields.add(child);
            }
        } catch (IllegalAccessException e) {
            // Fail object creation here.
            throw new InitializationException("Failed to access member of record");
        }
        return fields;
    }

    /**
     * Kahn's algorithm
     * DFS for topological sort
     * 1. Explore all reachable nodes.
     * 2. Count (hard) children.
     * 3. Count nodes able to initialize
     */
    private TopSortData<NODE> buildGraph(Object original, Class<?> targetClass) throws InitializationException {

        Queue<Object> front = new ArrayDeque<>();
        Set<Object> explored = new HashSet<>();

        Queue<Object> topologicalReady = new ArrayDeque<>();
        Map<Object, Integer> childCount = new HashMap<>();
        Map<Object, List<Object>> parents = new HashMap<>();

        front.add(original);
        NODE rootNode = nodeManager.createEmpty(targetClass, original);
        Map<Object, NODE> nodeMap = new HashMap<>();
        nodeMap.put(original, rootNode);
        while (!front.isEmpty()) {
            Object cur = front.poll();
            NODE curNode = nodeMap.get(cur);
            if (explored.contains(cur)) {
                continue;
            }
            explored.add(cur);
           if (cur instanceof Optional) {
               Optional<Object> optional = (Optional<Object>) cur;
               if(optional.isPresent()) {
                   childCount.put(cur, 1);
                   Object child = optional.get();
                   // Build parent pointer for topological sort
                   if (!parents.containsKey(child)) {
                       parents.put(child, new ArrayList<>());
                   }
                   parents.get(child).add(cur);
                   if (explored.contains(child)) {
                       curNode.addChild(nodeMap.get(child));
                       continue;
                   }
                   front.add(child);
                   NODE childNode = nodeManager.createEmpty(child.getClass(), child);
                   curNode.addChild(childNode);
                   nodeMap.put(child, childNode);
               } else {
                   topologicalReady.add(cur);
               }
           } else if (ClassUtils.isRecord(cur.getClass())) {
                List<Object> components = getRecordFields(cur);
                Field[] recordComponents = cur.getClass().getDeclaredFields();
                childCount.put(cur, components.size());
                for (int i = 0; i < recordComponents.length; i++) {
                    Object child = components.get(i);
                    // Build parent pointer for topological sort
                    if (!parents.containsKey(child)) {
                        parents.put(child, new ArrayList<>());
                    }
                    parents.get(child).add(cur);

                    if (explored.contains(child)) {
                        curNode.addChild(nodeMap.get(child));
                        continue;
                    }
                    front.add(child);
                    // Build object graph
                    // Use runtimeType due to type erasure
                    NODE childNode = nodeManager.createEmpty(recordComponents[i].getType(), child);
                    curNode.addChild(childNode);
                    nodeMap.put(child, childNode);
                }
            } else {
                if (cur.getClass().isArray()) {
                    Class<?> childrenClass = cur.getClass().getComponentType();
                    for (int i = 0; i < Array.getLength(cur); i++) {
                        Object child = Array.get(cur, i);
                        // TODO when null, get from cur.getClass().getComponentTpe
                        if (explored.contains(child)) {
                            curNode.addChild(nodeMap.get(child));
                            continue;
                        }
                        NODE childNode = nodeManager.createEmpty(childrenClass, child);
                        curNode.addChild(childNode);
                        nodeMap.put(child, childNode);
                        front.add(child);
                    }
                }
                topologicalReady.add(cur);
            }
        }
        return new TopSortData<>(topologicalReady, childCount, parents, nodeMap);
    }

    private Map<Object, Object> topOrderInit(TopSortData<NODE> topSortData) throws InitializationException {

        Queue<Object> ready = topSortData.topologicalReady();
        Map<Object, Integer> childCount = topSortData.childCount();
        Map<Object, Object> toWrapped = new HashMap<>();
        Map<Object, NODE> toNode = topSortData.nodeMap();

        while (!ready.isEmpty()) {
            Object cur = ready.poll();
            Object wrapped;
            if (cur.getClass().isArray()) {
                wrapped = Array.newInstance(cur.getClass().getComponentType(), Array.getLength(cur));
            } else if (ClassUtils.isRecord(cur.getClass())) {
                List<Object> components = getRecordFields(cur);
                List<Object> wrappedComponents = components.stream().map(toWrapped::get).collect(Collectors.toList());
                wrapped = ObjectInitializer.initRecord(cur.getClass(), wrappedComponents);
            } else if (cur instanceof Optional) {
                if(!((Optional<?>) cur).isPresent()) {
                    wrapped = cur;
                } else {
                    Object inner = ((Optional<?>) cur).get();
                    wrapped = Optional.of(toWrapped.get(inner));
                }
            } else {
                NODE node = toNode.get(cur);
                wrapped = nodeManager.synthesizeLeafNode(cur, node);
            }
            toWrapped.put(cur, wrapped);
            if (topSortData.parents().containsKey(cur)) {
                for (Object parent : topSortData.parents().get(cur)) {
                    int prevCount = topSortData.childCount().get(parent);
                    if (prevCount == 1) {
                        childCount.remove(parent);
                        ready.add(parent);
                    } else {
                        childCount.put(parent, prevCount - 1);
                    }
                }
            }
        }
        if (!childCount.isEmpty()) {
            throw new InitializationException("");
        }
        return toWrapped;
    }

    private void postAssignChildren(Map<Object, Object> toWrapped) {
        for (Object cur : toWrapped.keySet()) {
            Object wrapped = toWrapped.get(cur);
            if (cur.getClass().isArray()) {
                int len = Array.getLength(cur);
                for (int i = 0; i < len; i++) {
                    Object originElement = Array.get(cur, i);
                    Object wrappedElement = toWrapped.get(originElement);
                    Array.set(wrapped, i, wrappedElement);
                }
            }
        }
    }

    public <T> ReReWrapResult<T, NODE> createRoot(Object original, Class<?> targetClass) {
        /*
         *  1. DFS to find all objects in component.
         *  2. sort by child count
         *  3. create object map
         */
        if (original == null) {
            NODE node = nodeManager.createNull(targetClass);
            return new ReReWrapResult<>(null, node);
        }
        try {
            TopSortData<NODE> data = buildGraph(original, targetClass);
            Map<Object, Object> stuff = topOrderInit(data);
            postAssignChildren(stuff);
            T wrapped = (T) stuff.get(original);
            return new ReReWrapResult<>(wrapped, data.nodeMap().get(original));
        } catch (InitializationException e) {
            return new ReReWrapResult<>((T) original, nodeManager.createFailed(targetClass, ""));
        }
    }

    static class TopSortData<NODE> {
        protected final Queue<Object> topologicalReady;
        protected final Map<Object, Integer> childCount;
        protected final Map<Object, List<Object>> parents;
        protected final Map<Object, NODE> nodeMap;

        public TopSortData(Queue<Object> topologicalReady,
                           Map<Object, Integer> childCount,
                           Map<Object, List<Object>> parents,
                           Map<Object, NODE> nodeMap) {
            this.topologicalReady = topologicalReady;
            this.childCount = childCount;
            this.parents = parents;
            this.nodeMap = nodeMap;
        }

        public Queue<Object> topologicalReady() {
            return topologicalReady;
        }

        public Map<Object, Integer> childCount() {
            return childCount;
        }

        public Map<Object, List<Object>> parents() {
            return parents;
        }

        public Map<Object, NODE> nodeMap() {
            return nodeMap;
        }

    }
}
