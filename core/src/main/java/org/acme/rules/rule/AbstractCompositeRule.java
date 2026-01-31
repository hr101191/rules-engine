package org.acme.rules.rule;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import org.acme.rules.model.CompositeRuleDescriptor;
import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.model.RuleTraceMetadata;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public sealed abstract class AbstractCompositeRule implements Rule permits AndRule, AndAlsoRule, OrRule, OrElseRule {

    @Getter
    private final CompositeRuleDescriptor ruleDescriptor;

    @Getter
    private final List<Rule> rules;

    private final MeterRegistry meterRegistry;

    protected AbstractCompositeRule(@NonNull CompositeRuleDescriptor ruleDescriptor, List<Rule> rules, boolean metricsEnabled) {
        this.ruleDescriptor = Objects.requireNonNull(ruleDescriptor);
        this.rules = Objects.requireNonNullElse(rules, List.of());
        if (metricsEnabled) {
            this.meterRegistry = Metrics.globalRegistry;
        } else {
            this.meterRegistry = null;
        }
    }

    @Override
    public final RuleTrace execute(Map<String, Object> input, Map<String, Object> globalParams, EvaluationContext evaluationContext) {
        RuleTrace ruleTrace = executeInternal(input, globalParams, evaluationContext);
        return attachMetadata(ruleTrace);
    }

    @Override
    public final String getRuleName() {
        return ruleDescriptor.getRuleName();
    }

    protected abstract RuleTrace executeInternal(Map<String, Object> input, Map<String, Object> globalParams, EvaluationContext evaluationContext);

    protected void recordMetrics(long durationNanos, RuleExecutionStatus status) {
        if (this.meterRegistry != null && status != RuleExecutionStatus.DISABLED) {
            this.meterRegistry.timer("rule_evaluation_time", "status", status.name(), "rule_name", getRuleName(), "type", "composite").record(durationNanos, TimeUnit.NANOSECONDS);
        }
    }

}
