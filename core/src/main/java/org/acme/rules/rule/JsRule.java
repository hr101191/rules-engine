package org.acme.rules.rule;

import lombok.Builder;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public final class JsRule extends AbstractExpressionRule {

    private final ParameterExtractor parameterExtractor;

    @Builder
    public JsRule(@NonNull ExpressionRuleDescriptor ruleDescriptor, boolean metricsEnabled, ParameterExtractor parameterExtractor) {
        super(ruleDescriptor, metricsEnabled);
        this.parameterExtractor = parameterExtractor;
    }

    @Override
    protected boolean executeInternal(Map<String, Object> input, Map<String, Object> globalParams) throws Exception {
        return false;
    }

}
