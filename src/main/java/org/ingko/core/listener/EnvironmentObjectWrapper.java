package org.ingko.core.listener;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class EnvironmentObjectWrapper {
    private final EnvironmentObjectListener environmentObjectListener;
    public EnvironmentObjectWrapper(EnvironmentObjectListener environmentObjectListener) {
        this.environmentObjectListener = environmentObjectListener;
    }


    private void exploreRecord(Object cur,
                               Queue<Object> front,
                               Map<Object, List<Object>> parents,
                               Map<Object, Integer> childCount) {
        RecordComponent[] recordComponents = cur.getClass().getRecordComponents();
        childCount.put(cur, recordComponents.length);
        for(RecordComponent field: recordComponents) {
            try {
                Object child = field.getAccessor().invoke(cur);
                if(!parents.containsKey(child)) {
                    parents.put(child, new ArrayList<>());
                }
                parents.get(child).add(cur);
                front.add(child);
            } catch (IllegalAccessException| InvocationTargetException e) {
                // Fail object creation here.
            }
        }
    }

    private void exploreArray(Object cur, Queue<Object> front) {
        int size = Array.getLength(cur);
        for(int i = 0; i < size; i++) {
            Object child = Array.get(cur, i);
            front.add(child);
        }
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        /*
         *  1. DFS to find all objects in component.
         *  2. sort by child count
         *  3. create object map
         */
        Queue<Object> front = new ArrayDeque<>();
        Map<Object, Object> originToSpied = new HashMap<>();
        Map<Object, List<Object>> parents = new HashMap<>();
        Map<Object, Integer> childCount = new HashMap<>();
        Queue<Object> readyToSpy = new ArrayDeque<>();

        front.add(original);
        while (!front.isEmpty()){
            Object cur = front.poll();
            if (originToSpied.containsKey(cur)) {
                continue;
            }
            originToSpied.put(cur, null);
            if (cur.getClass().isRecord()) {
                exploreRecord(cur, front, parents, childCount);
            } else if (cur.getClass().isArray()) {
                exploreArray(cur, front);
                readyToSpy.add(cur);
            } else {
                readyToSpy.add(cur);
            }
        }
        // first round spy
        while(!readyToSpy.isEmpty()) {
            Object cur = readyToSpy.poll();
            if(cur.getClass().isArray()) {
                //Create, but not fill
                Object array = Array.newInstance(cur.getClass().getComponentType(),
                        Array.getLength(cur));
                originToSpied.put(cur, array);
            } else if (cur.getClass().isRecord()) {
                // full initialize

            } else {

            }

        }

        return (T)original;
    }



}
