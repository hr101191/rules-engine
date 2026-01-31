package org.acme.rules.rule;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.model.CompositeRuleDescriptor;
import org.acme.rules.model.RuleExecutionStatus;
import org.acme.rules.model.RuleTrace;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class AndAlsoRule extends AbstractCompositeRule {

    private final ParameterExtractor parameterExtractor;

    @Builder
    AndAlsoRule(@NonNull CompositeRuleDescriptor ruleDescriptor, List<Rule> rules, boolean metricsEnabled, ParameterExtractor parameterExtractor) {
        super(ruleDescriptor, rules, metricsEnabled);
        this.parameterExtractor = parameterExtractor;
    }

    @Override
    protected RuleTrace executeInternal(Map<String, Object> input, Map<String, Object> globalParams, EvaluationContext evaluationContext) {
        long start = System.nanoTime();
        evaluationContext.enterRule(super.getRuleName());
        RuleExecutionStatus executionStatus = RuleExecutionStatus.SUCCESS;
        boolean compositeResult = false;
        int rulesExecuted = 0;
        int size = super.getRules().size();
        try {
            log.info("Executing rule: {}", super.getRuleName());
            if (super.getRuleDescriptor().isEnabled()) {
                Map<String, Object> combinedGlobalParam = new HashMap<>(globalParams);
                if (parameterExtractor != null) {
                    combinedGlobalParam.putAll(parameterExtractor.extract(input, globalParams));
                }
                globalParams = combinedGlobalParam;
                for (Rule rule : super.getRules()) {
                    rule.execute(input, globalParams, evaluationContext);
                    rulesExecuted ++;
                    RuleTrace ruleTrace = evaluationContext.getCurrentTrace();
                    if (ruleTrace.getChildren() != null) {
                        compositeResult = ruleTrace.getChildren().stream().allMatch(RuleTrace::isResult);
                        if (!compositeResult) { //And also condition -> short circuit early on the first false
                            break;
                        }
                    }
                }
            } else {
                executionStatus = RuleExecutionStatus.DISABLED;
            }
            long end = System.nanoTime() - start;
            super.recordMetrics(end, executionStatus);
            log.info("Rule name: {} - Successfully executed Composite Rule Expression [AND_ALSO] | Result: {}", super.getRuleName(), compositeResult);
            return evaluationContext.exitRule(compositeResult, rulesExecuted != size, end, executionStatus);
        } catch (Exception ex) {
            long end = System.nanoTime() - start;
            log.error("Rule name: {} - Composite Rule Expression [AND_ALSO] execution failed. Stacktrace: ", super.getRuleName(), ex);
            executionStatus = RuleExecutionStatus.ERROR;
            super.recordMetrics(end, executionStatus);
            return evaluationContext.exitRule(compositeResult, rulesExecuted != size, end, executionStatus);
        }
    }
}
