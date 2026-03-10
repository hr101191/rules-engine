package org.acme.rules.compiler.module;

import org.acme.rules.compiler.factory.CompositeRuleFactory;
import org.acme.rules.compiler.factory.RuleFactory;

public class CompositeRuleModule implements RuleModule {

    @Override
    public RuleFactory ruleFactory() {
        return new CompositeRuleFactory();
    }

}
