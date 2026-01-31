package org.acme.rules;

import io.vertx.core.Future;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.workflow.WorkflowKey;

import java.util.List;
import java.util.Map;

public interface RuleEngine {

    static RuleEngineImpl.RuleEngineImplBuilder builder() {
        return RuleEngineImpl.builder();
    }

    Future<List<RuleTrace>> executeWorkflow(WorkflowKey workflowKey, Map<String, Object> input);
}
