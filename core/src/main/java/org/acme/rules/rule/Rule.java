package org.acme.rules.rule;

import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.RuleTrace;

import java.util.Map;

public sealed interface Rule permits AbstractCompositeRule, AbstractExpressionRule {

    RuleTrace execute(Map<String, Object> input, Map<String, Object> globalParams, EvaluationContext evaluationContext);

    String getRuleName();

    RuleDescriptor getRuleDescriptor();

    default RuleTrace attachMetadata(RuleTrace ruleTrace) {
//        RuleTraceMetadata ruleTraceMetadata = getRuleDescriptor().getRuleTraceMetadata();
//        if (ruleTraceMetadata != null) {
//            if (ruleTraceMetadata.isReturnOnTrue() && ruleTrace.isResult()) {
//                if (ruleTraceMetadata.getMetadata() != null) {
//                    ruleTrace.setMetadata(ruleTraceMetadata.getMetadata());
//                }
//            } else if (ruleTraceMetadata.isReturnOnFalse() && !ruleTrace.isResult()) {
//                if (ruleTraceMetadata.getMetadata() != null) {
//                    ruleTrace.setMetadata(ruleTraceMetadata.getMetadata());
//                }
//            }
//        }
        return ruleTrace;
    }

}
