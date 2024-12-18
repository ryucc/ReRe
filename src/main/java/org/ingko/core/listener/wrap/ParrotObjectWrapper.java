package org.ingko.core.listener.wrap;

import org.ingko.core.data.objects.ParrotObjectNode;
import org.ingko.core.listener.NodeManager;
import org.ingko.core.listener.ObjectInitializer;
import org.ingko.core.listener.exceptions.InitializationException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @param <NODE>
 * @param <MANAGER>
 */
public class ParrotObjectWrapper<NODE extends ParrotObjectNode, MANAGER extends NodeManager<NODE>> {
    private final MANAGER nodeManager;

    public ParrotObjectWrapper(MANAGER nodeManager) {
        this.nodeManager = nodeManager;
    }


    public List<Object> getRecordFields(Object cur) throws InitializationException {
        RecordComponent[] recordComponents = cur.getClass().getRecordComponents();
        List<Object> fields = new ArrayList<>();

        try {
            for (RecordComponent field : recordComponents) {
                Object child = field.getAccessor().invoke(cur);
                fields.add(child);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
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
        NODE rootNode = nodeManager.createEmpty(targetClass);
        Map<Object, NODE> nodeMap = new HashMap<>();
        nodeMap.put(original, rootNode);
        while (!front.isEmpty()) {
            Object cur = front.poll();
            NODE curNode = nodeMap.get(cur);
            if (explored.contains(cur)) {
                continue;
            }
            explored.add(cur);
            if (cur.getClass().isRecord()) {
                List<Object> components = getRecordFields(cur);
                RecordComponent[] recordComponents = cur.getClass().getRecordComponents();
                childCount.put(cur, components.size());
                for (int i = 0; i < recordComponents.length; i++) {
                    Object child = components.get(i);
                    RecordComponent recordComponent = recordComponents[i];
                    // Build parent pointer for topological sort
                    if (!parents.containsKey(child)) {
                        parents.put(child, new ArrayList<>());
                    }
                    parents.get(child).add(cur);
                    front.add(child);
                    // Build object graph
                    // Use runtimeType due to type erasure
                    NODE childNode = nodeManager.createEmpty(recordComponent.getType());
                    nodeManager.addChild(curNode, childNode);
                    nodeMap.put(child, childNode);
                }
            } else {
                if (cur.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(cur); i++) {
                        Object child = Array.get(cur, i);
                        NODE childNode = nodeManager.createEmpty(targetClass);
                        nodeManager.addChild(curNode, childNode);
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
            } else if (cur.getClass().isRecord()) {
                List<Object> components = getRecordFields(cur);
                List<Object> wrappedComponents = components.stream().map(toWrapped::get).toList();
                wrapped = ObjectInitializer.initRecord(cur.getClass(), wrappedComponents);
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

    public void postAssignChildren(Map<Object, Object> toWrapped) {
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

    public <T> WrapResult<T, NODE> createRoot(Object original, Class<?> targetClass) {
        /*
         *  1. DFS to find all objects in component.
         *  2. sort by child count
         *  3. create object map
         */
        if (original == null) {
            NODE node = nodeManager.createNull(targetClass);
            return new WrapResult<>(null, node);
        }
        try {
            TopSortData<NODE> data = buildGraph(original, targetClass);
            Map<Object, Object> stuff = topOrderInit(data);
            postAssignChildren(stuff);
            T wrapped = (T) stuff.get(original);
            return new WrapResult<>(wrapped, data.nodeMap().get(original));
        } catch (InitializationException e) {
            return new WrapResult<>((T) original, nodeManager.createFailed(targetClass, ""));
        }
    }

    record TopSortData<NODE>(Queue<Object> topologicalReady, Map<Object, Integer> childCount,
                             Map<Object, List<Object>> parents, Map<Object, NODE> nodeMap) {
    }

    public record WrapResult<T, NODE>(T wrapped, NODE node) {
    }

}
