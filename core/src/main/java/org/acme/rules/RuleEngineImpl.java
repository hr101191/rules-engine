package org.acme.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cel.runtime.CelRuntime;
import dev.cel.runtime.CelRuntimeBuilder;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.cel.CelRuntimeBuilderContainer;
import org.acme.rules.cel.CelRuntimeBuilderCustomizer;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.model.WorkflowDescriptor;
import org.acme.rules.workflow.Workflow;
import org.acme.rules.workflow.WorkflowExecutor;
import org.acme.rules.workflow.WorkflowKey;
import org.acme.rules.workflow.WorkflowRegistry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class RuleEngineImpl implements RuleEngine {

    @Getter
    private final WorkflowExecutor workflowExecutor;

    @Builder
    public RuleEngineImpl(@NonNull Vertx vertx, @Nullable List<CelRuntimeBuilderCustomizer> celRuntimeBuilderCustomizers, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        this.workflowExecutor = WorkflowExecutor.builder().vertx(vertx).build();
        CelRuntimeBuilder celRuntimeBuilder = CelRuntimeBuilderContainer.INSTANCE;
        if (celRuntimeBuilderCustomizers != null) {
            celRuntimeBuilderCustomizers.forEach(celRuntimeBuilderCustomizer -> celRuntimeBuilderCustomizer.customize(celRuntimeBuilder));
        }
        CelRuntime celRuntime = celRuntimeBuilder.build();
        AtomicBoolean running = new AtomicBoolean(false);
        vertx.setPeriodic(0L, 30000L, handler -> {
            if (running.compareAndSet(false, true)) {
                try {
                    log.info("Executing scheduled task...");
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, List<WorkflowDescriptor>> descriptorByRealm = objectMapper.readValue(DemoWorkflow.WORKFLOW, new TypeReference<>() {
                    });
                    for (Map.Entry<String, List<WorkflowDescriptor>> entry : descriptorByRealm.entrySet()) {
                        for (WorkflowDescriptor workflowDescriptor : entry.getValue()) {
                            WorkflowKey workflowKey = WorkflowKey.builder().realm(entry.getKey()).workflowName(workflowDescriptor.getWorkflowName()).build();
                            if (!WorkflowRegistry.contains(workflowKey)) {
                                Workflow workflow = Workflow.builder()
                                        .realm(entry.getKey())
                                        .workflowDescriptor(workflowDescriptor)
                                        .lastUpdated(0)
                                        .celRuntime(celRuntime)
                                        .celCompilerBuilderCustomizers(celCompilerBuilderCustomizers)
                                        .build();
                                WorkflowRegistry.put(workflowKey, workflow);
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("Failed to execute scheduled execution Stacktrace: ", ex);
                } finally {
                    running.set(false);
                }
            }
        });
    }

    @Override
    public Future<List<RuleTrace>> executeWorkflow(WorkflowKey workflowKey, Map<String, Object> input) {
        return workflowExecutor.execute(workflowKey, input);
    }
}
