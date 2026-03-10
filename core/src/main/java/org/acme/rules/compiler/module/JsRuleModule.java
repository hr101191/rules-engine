package org.acme.rules.compiler.module;

import org.acme.rules.compiler.factory.JsRuleFactory;
import org.acme.rules.compiler.factory.RuleFactory;

public class JsRuleModule implements RuleModule {

    @Override
    public RuleFactory ruleFactory() {
        return new JsRuleFactory();
    }

}
