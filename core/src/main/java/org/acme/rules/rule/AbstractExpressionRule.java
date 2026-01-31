package org.acme.rules.rule;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;
import org.jspecify.annotations.NonNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public sealed abstract class AbstractExpressionRule implements Rule permits CelRule, JsRule, NoopRule {

    @Getter
    private final ExpressionRuleDescriptor ruleDescriptor;
    private final MeterRegistry meterRegistry;

    public AbstractExpressionRule(@NonNull ExpressionRuleDescriptor ruleDescriptor, boolean metricsEnabled) {
        this.ruleDescriptor = Objects.requireNonNull(ruleDescriptor);
        if (metricsEnabled) {
            this.meterRegistry = Metrics.globalRegistry;
        } else {
            this.meterRegistry = null;
        }
    }

    @Override
    public final RuleTrace execute(Map<String, Object> input, Map<String, Object> globalParams, EvaluationContext evaluationContext) {
        long start = System.nanoTime();
        evaluationContext.enterRule(ruleDescriptor.getRuleName());
        RuleExecutionStatus executionStatus = RuleExecutionStatus.SUCCESS;
        boolean result = false;
        try {
            if (ruleDescriptor.isEnabled()) {
                result = executeInternal(input, globalParams);
            } else {
                executionStatus = RuleExecutionStatus.DISABLED;
            }
            long end = System.nanoTime() - start;
            recordMetrics(end, executionStatus);
            return attachMetadata(evaluationContext.exitRule(result, end, executionStatus));
        } catch (Exception ex) {
            long end = System.nanoTime() - start;
            log.error("Rule name: {} - Expression evaluation execution failed. Stacktrace: ", getRuleName(), ex);
            executionStatus = RuleExecutionStatus.ERROR;
            return attachMetadata(evaluationContext.exitRule(result, end, executionStatus));
        }
    }

    @Override
    public final String getRuleName() {
        return ruleDescriptor.getRuleName();
    }

    protected abstract boolean executeInternal(Map<String, Object> input, Map<String, Object> globalParams) throws Exception;

    private void recordMetrics(long durationNanos, RuleExecutionStatus status) {
        if (this.meterRegistry != null && status != RuleExecutionStatus.DISABLED) {
            this.meterRegistry.timer("rule_evaluation_time", "status", status.name(), "rule_name", getRuleName(), "type", "expression").record(durationNanos, TimeUnit.NANOSECONDS);
        }
    }

}
