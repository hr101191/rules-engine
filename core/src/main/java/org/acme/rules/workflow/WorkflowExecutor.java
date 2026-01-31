package org.acme.rules.workflow;

import io.vertx.core.Future;
import org.acme.rules.model.RuleTrace;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public interface WorkflowExecutor {

    static WorkflowExecutorImpl.WorkflowExecutorImplBuilder builder() {
        return WorkflowExecutorImpl.builder();
    }

    Future<List<RuleTrace>> execute(@NonNull WorkflowKey workflowKey, @NonNull Map<String, Object> input);

    Future<List<RuleTrace>> execute(@NonNull Workflow workflow, @NonNull Map<String, Object> input);

}
