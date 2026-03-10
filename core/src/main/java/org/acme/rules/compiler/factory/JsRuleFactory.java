package org.acme.rules.compiler.factory;

import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.rule.JsRule;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

public class JsRuleFactory implements RuleFactory {

    @Override
    public boolean supports(RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof ExpressionRuleDescriptor expressionRuleDescriptor
                && expressionRuleDescriptor.getRuleExpressionType().equals(RuleExpressionType.JS);
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler) {
        return JsRule.builder().ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor).build();
    }
}
