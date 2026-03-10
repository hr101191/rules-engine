package org.acme.rules.compiler.factory;

import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.model.CompositeRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.rule.*;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompositeRuleFactory implements RuleFactory {

    @Override
    public boolean supports(RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof CompositeRuleDescriptor;
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler) {
        CompositeRuleDescriptor compositeRuleDescriptor = (CompositeRuleDescriptor) ruleDescriptor;
        List<Rule> children = Objects.requireNonNullElseGet(compositeRuleDescriptor.getRules(), () -> new ArrayList<RuleDescriptor>())
                .stream()
                .map(ruleCompiler::compile)
                .toList();
        return switch (compositeRuleDescriptor.getOperator()) {
            case AND -> AndRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).build();
            case AND_ALSO -> AndAlsoRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).build();
            case OR -> OrRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).build();
            case OR_ELSE -> OrElseRule.builder().ruleDescriptor(compositeRuleDescriptor).rules(children).build();
        };
    }
}
