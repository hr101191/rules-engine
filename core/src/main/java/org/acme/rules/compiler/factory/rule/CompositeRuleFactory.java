package org.acme.rules.compiler.factory.rule;

import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.compiler.factory.RuleFactory;
import org.acme.rules.model.CompositeRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositeRuleFactory implements RuleFactory {

    @Override
    public boolean supports(@NonNull RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof CompositeRuleDescriptor;
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler, @Nullable ParameterExtractor parameterExtractor) {
        CompositeRuleDescriptor compositeRuleDescriptor = (CompositeRuleDescriptor) ruleDescriptor;
        List<Rule> children = Objects.requireNonNullElseGet(compositeRuleDescriptor.getRules(), () -> new ArrayList<RuleDescriptor>())
                .stream()
                .map(ruleCompiler::compile)
                .toList();
        return switch (compositeRuleDescriptor.getOperator()) {
            case AND -> AndRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).parameterExtractor(parameterExtractor).build();
            case AND_ALSO -> AndAlsoRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).parameterExtractor(parameterExtractor).build();
            case OR -> OrRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).parameterExtractor(parameterExtractor).build();
            case OR_ELSE -> OrElseRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).parameterExtractor(parameterExtractor).build();
        };
    }
}
