package org.acme.rules.compiler.factory.rule;

import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.compiler.factory.RuleFactory;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.JsRule;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class JsRuleFactory implements RuleFactory {

    @Override
    public boolean supports(@NonNull RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof ExpressionRuleDescriptor expressionRuleDescriptor
                && expressionRuleDescriptor.getRuleExpressionType().equals(RuleExpressionType.JS);
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler, @Nullable ParameterExtractor parameterExtractor) {
        return JsRule.builder()
                .ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor)
                .parameterExtractor(parameterExtractor)
                .build();
    }
}
