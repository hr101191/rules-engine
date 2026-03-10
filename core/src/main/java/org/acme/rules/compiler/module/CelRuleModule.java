package org.acme.rules.compiler.module;

import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.CelRuleFactory;
import org.acme.rules.compiler.factory.RuleFactory;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CelRuleModule implements RuleModule {

    private final boolean metricsEnabled;
    private final List<String> globalParamNames;
    private final List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers;

    public CelRuleModule(boolean metricsEnabled, @Nullable List<String> globalParamNames, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        this.metricsEnabled = metricsEnabled;
        this.globalParamNames = globalParamNames;
        this.celCompilerBuilderCustomizers = celCompilerBuilderCustomizers;
    }

    @Override
    public RuleFactory ruleFactory() {
        return new CelRuleFactory(metricsEnabled, globalParamNames, celCompilerBuilderCustomizers);
    }

}
