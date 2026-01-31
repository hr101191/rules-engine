package org.acme.rules.rule;

import lombok.Builder;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public final class NoopRule extends AbstractExpressionRule {

    @Builder
    public NoopRule(@NonNull ExpressionRuleDescriptor ruleDescriptor, boolean metricsEnabled) {
        super(ruleDescriptor, metricsEnabled);
    }

    @Override
    protected boolean executeInternal(Map<String, Object> input, Map<String, Object> globalParams) throws Exception {
        return false;
    }

}
