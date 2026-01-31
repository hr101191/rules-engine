package org.acme.rules.workflow;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.rule.EvaluationContext;
import org.acme.rules.task.TaskExecutor;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

@Slf4j
public class WorkflowExecutorImpl implements WorkflowExecutor {

    private final TaskExecutor taskExecutor;

    @Builder
    public WorkflowExecutorImpl(@NonNull Vertx vertx) {
        Objects.requireNonNull(vertx);
        this.taskExecutor = TaskExecutor.builder().vertx(vertx).build();
    }

    @Override
    public Future<List<RuleTrace>> execute(@NonNull WorkflowKey workflowKey, @NonNull Map<String, Object> input) {
        return execute(Objects.requireNonNull(WorkflowRegistry.get(workflowKey)), input);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Future<List<RuleTrace>> execute(@NonNull Workflow workflow, @NonNull Map<String, Object> input) {
        Promise<List<RuleTrace>> promise = Promise.promise();
        try {
            Objects.requireNonNull(workflow);
            Objects.requireNonNull(input);
            log.info("Executing all rules from workflow...");
            if (workflow.isEnabled()) {
                List<Workflow> workflowsToExecute = new ArrayList<>();
                List<Future<List<RuleTrace>>> resultsFuture = new ArrayList<>();
                workflowsToExecute.add(workflow);
                if (workflow.getWorkflowDescriptor().getWorkflowsToInject() != null) {
                    for (String workflowToInject : workflow.getWorkflowDescriptor().getWorkflowsToInject()) {
                        Workflow injectedWorkflow = WorkflowRegistry.get(WorkflowKey.builder().workflowName(workflowToInject).realm(workflow.getRealm()).build());
                        if (injectedWorkflow != null) {
                            workflowsToExecute.add(injectedWorkflow);
                        }
                    }
                }
                for (Workflow workflowToExecute : workflowsToExecute) {
                    Future<List<RuleTrace>> results = taskExecutor.execute(() -> workflowToExecute.extractGlobalParameters(input))
                            .compose(globalParams -> {
                                List<Callable<RuleTrace>> tasks = workflowToExecute.getRules()
                                        .stream()
                                        .map(rule -> (Callable<RuleTrace>) () -> rule.execute(input, globalParams, new EvaluationContext()))
                                        .toList();
                                return taskExecutor.execute(tasks);
                            });
                    resultsFuture.add(results);
                }
                Future.join(resultsFuture)
                        .compose(compositeFuture -> {
                            List<RuleTrace> list = IntStream.range(0, compositeFuture.size())
                                    .mapToObj(i -> (List<RuleTrace>) compositeFuture.resultAt(i))
                                    .flatMap(List::stream)
                                    .toList();
                            return Future.succeededFuture(list);
                        })
                        .onComplete(asyncResult -> {
                            if (asyncResult.succeeded()) {
                                promise.complete(asyncResult.result());
                            } else {
                                promise.fail(asyncResult.cause());
                            }
                        });
            } else {
                promise.complete(new ArrayList<>());
            }
        } catch (Exception ex) {
            promise.fail(ex);
        }
        return promise.future();
    }

}
