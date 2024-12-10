package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.exceptions.InitializationException;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.core.serde.exceptions.SerializationException;

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

//TODO make template
// T, Setter<T>, create interface for setter
public class EnvironmentObjectWrapper {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    private final EnvironmentObjectListener environmentObjectListener;
    ClassRepo classRepo;

    public EnvironmentObjectWrapper(EnvironmentObjectListener environmentObjectListener) {

        classRepo = new ClassRepo(environmentObjectListener,
                Map.of(EnvironmentObjectSpy.FIELD, EnvironmentObjectSpy.TYPE, ObjectSpy.FIELD, ObjectSpy.TYPE),
                List.of(EnvironmentObjectSpy.class, ObjectSpy.class));
        this.environmentObjectListener = environmentObjectListener;
    }

    public <T> T handleInternal(T returnValue, EnvironmentNode node) {
        try {
            Class<?> mockedClass = classRepo.getOrDefineSubclass(returnValue.getClass());
            T mocked = (T) ObjectInitializer.create(mockedClass);
            ((EnvironmentObjectSpy) mocked).setParrotNodePointer(node);
            ((EnvironmentObjectSpy) mocked).setParrotOriginObject(returnValue);
            return mocked;
        } catch (Exception e) {
            node.setFailedNode(true);
            return returnValue;
        }
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
     *
     * Kahn's algorithm
     * DFS for topological sort
     * 1. Explore all reachable nodes.
     * 2. Count (hard) children.
     * 3. Count nodes able to initialize
     */
    private TopSortData buildGraph(Object original, Class<?> targetClass) throws InitializationException {

        Queue<Object> front = new ArrayDeque<>();
        Set<Object> explored = new HashSet<>();

        Queue<Object> topologicalReady = new ArrayDeque<>();
        Map<Object, Integer> childCount = new HashMap<>();
        Map<Object, List<Object>> parents = new HashMap<>();

        front.add(original);
        EnvironmentNode rootNode = EnvironmentNode.ofInternal(targetClass);
        Map<Object, EnvironmentNode> nodeMap = new HashMap<>();
        nodeMap.put(original, rootNode);
        while (!front.isEmpty()) {
            Object cur = front.poll();
            System.out.println(cur);
            EnvironmentNode curNode = nodeMap.get(cur);
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
                    EnvironmentNode childNode = EnvironmentNode.ofInternal(recordComponent.getType());
                    curNode.addDirectChild(childNode);
                    nodeMap.put(child, childNode);
                }
            } else {
                if (cur.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(cur); i++) {
                        Object child = Array.get(cur, i);
                        EnvironmentNode childNode = EnvironmentNode.ofInternal(cur.getClass().getComponentType());
                        nodeMap.put(child, childNode);
                        front.add(child);
                    }
                }
                topologicalReady.add(cur);
            }
        }
        return new TopSortData(topologicalReady, childCount, parents, nodeMap);
    }

    public Map<Object, Object> topOrderInit(TopSortData topSortData) throws InitializationException {

        Queue<Object> ready = topSortData.topologicalReady();
        Map<Object, Integer> childCount = topSortData.childCount();
        Map<Object, Object> toWrapped = new HashMap<>();
        Map<Object, EnvironmentNode> toNode = topSortData.nodeMap();

        while (!ready.isEmpty()) {
            Object cur = ready.poll();
            Object wrapped;
            if (cur.getClass().isArray()) {
                wrapped = Array.newInstance(cur.getClass().getComponentType(), Array.getLength(cur));
            } else if (cur.getClass().isRecord()) {
                List<Object> components = getRecordFields(cur);
                List<Object> wrappedComponents = components.stream()
                        .map(component -> toWrapped.get(component))
                        .toList();
                wrapped = ObjectInitializer.initRecord(cur.getClass(), wrappedComponents);
            } else if (ClassUtils.isString(cur.getClass())) {
                EnvironmentNode curNode = toNode.get(cur);
                curNode.setValue("\"" + cur + "\"");
                curNode.setTerminal(true);
                wrapped = cur;
            } else if (ClassUtils.isStringOrPrimitive(cur.getClass())) {
                EnvironmentNode curNode = toNode.get(cur);
                curNode.setValue(cur.toString());
                curNode.setTerminal(true);
                wrapped = cur;
            } else if (Throwable.class.isAssignableFrom(cur.getClass())) {
                EnvironmentNode curNode = toNode.get(cur);
                curNode.setTerminal(true);
                curNode.setSerialized(true);
                try {
                    curNode.setValue(defaultSerde.serialize(cur));
                } catch (SerializationException e) {
                    curNode.setFailedNode(true);
                    curNode.setComments(e.getMessage());
                }
                wrapped = cur;
            } else {
                wrapped = handleInternal(cur, toNode.get(cur));
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

    public <T> WrapResult<T> createRoot(Object original, Class<T> targetClass) {
        /*
         *  1. DFS to find all objects in component.
         *  2. sort by child count
         *  3. create object map
         */
        if (original == null) {
            EnvironmentNode node = EnvironmentNode.ofNull(targetClass);
            return new WrapResult<>(null, node);
        }
        try {
            TopSortData data = buildGraph(original, targetClass);
            Map<Object, Object> stuff = topOrderInit(data);
            postAssignChildren(stuff);
            T wrapped = (T) stuff.get(original);
            return new WrapResult<>(wrapped, data.nodeMap().get(original));
        } catch (InitializationException e) {
            return new WrapResult<>((T) original, EnvironmentNode.ofFailed(targetClass, ""));
        }
    }

    record TopSortData(Queue<Object> topologicalReady, Map<Object, Integer> childCount,
                       Map<Object, List<Object>> parents, Map<Object, EnvironmentNode> nodeMap) {
    }

    public record WrapResult<T>(T wrapped, EnvironmentNode dataEnvironmentNode) {
    }

}
