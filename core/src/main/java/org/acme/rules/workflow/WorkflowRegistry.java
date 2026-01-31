package org.acme.rules.workflow;

import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class WorkflowRegistry {

    private static final Map<WorkflowKey, Workflow> CACHE = new ConcurrentHashMap<>();

    public static boolean contains(WorkflowKey key) {
        return CACHE.containsKey(key);
    }

    @Nullable
    public static Workflow get(WorkflowKey workflowKey) {
        return CACHE.get(workflowKey);
    }

    public static void put(WorkflowKey key, Workflow workflow) {
        CACHE.put(key, workflow);
    }

    public static void remove(WorkflowKey workflowKey) {
        CACHE.remove(workflowKey);
    }

}
