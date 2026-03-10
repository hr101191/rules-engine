package org.acme.rules.compiler.module;

import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.compiler.factory.RuleFactory;

public class JsRuleModule implements RuleModule {

    @Override
    public RuleFactory ruleFactory() {
        return RuleFactory.createJsRuleFactory();
    }

    @Override
    public ParameterExtractorFactory parameterExtractorFactory() {
        return ParameterExtractorFactory.createJsParameterExtractorFactory();
    }

}
