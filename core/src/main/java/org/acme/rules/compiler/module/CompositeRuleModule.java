package org.acme.rules.compiler.module;

import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.compiler.factory.RuleFactory;

public class CompositeRuleModule implements RuleModule {

    @Override
    public RuleFactory ruleFactory() {
        return RuleFactory.createCompositeRuleFactory();
    }

    @Override
    public ParameterExtractorFactory parameterExtractorFactory() {
        return null;
    }

}
