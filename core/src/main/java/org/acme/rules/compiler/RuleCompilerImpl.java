package org.acme.rules.compiler;

import org.acme.rules.compiler.factory.RuleFactory;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class RuleCompilerImpl implements RuleCompiler {

    private final List<RuleFactory> ruleFactories;

    public RuleCompilerImpl(List<RuleFactory> ruleFactories) {
        this.ruleFactories = ruleFactories;
    }

    @Override
    public Rule compile(@NonNull RuleDescriptor ruleDescriptor) {
        for (RuleFactory ruleFactory : ruleFactories) {
            if (ruleFactory.supports(ruleDescriptor)) {
                return ruleFactory.create(ruleDescriptor, this, null);
            }
        }
        throw new UnsupportedOperationException("Unsupported rule type: " + ruleDescriptor);
    }

    @Override
    public RuleCompiler addRuleFactory(@NonNull RuleFactory ruleFactory) {
        ruleFactories.add(ruleFactory);
        return this;
    }
}
