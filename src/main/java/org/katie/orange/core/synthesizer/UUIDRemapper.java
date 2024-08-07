package org.katie.orange.core.synthesizer;

import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.objects.ThrowableNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.UUID;

public class UUIDRemapper {
    /*
    public static Map<UUID, Long> buildMap(ObjectNode root) {
        List<UUID> uuids = new ArrayList<>();
        Stack<ObjectNode> front = new Stack<>();
        front.push(root);

        while(!front.empty()) {
            ObjectNode cur = front.pop();
            if(cur instanceof ThrowableNode) {
                uuids.add(((ThrowableNode) cur).getUuid());
                continue;
            }
            if (cur instanceof InternalNode) {
                uuids.add(((InternalNode) cur).getUuid());
                for (InternalMethodCall methodCallEdge: cur.getEdges()) {
                    front.push(methodCallEdge.getDest());
                }
            }
        }
        SortedMap<Long, UUID> longToUUID = new TreeMap<>();

        for(UUID uuid: uuids) {
            longToUUID.put(uuid.getMostSignificantBits(), uuid);
        }

        Map<UUID, Long> mapping = new HashMap<>();
        long cur_id = 1;
        for(Long key: longToUUID.keySet()) {
            mapping.put(longToUUID.get(key), cur_id);
            cur_id++;
        }
        return mapping;
    }
     */
}
