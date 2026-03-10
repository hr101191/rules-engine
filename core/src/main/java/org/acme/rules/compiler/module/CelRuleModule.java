package org.acme.rules.compiler.module;

import dev.cel.runtime.CelRuntime;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.compiler.factory.rule.CelRuleFactory;
import org.acme.rules.compiler.factory.RuleFactory;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CelRuleModule implements RuleModule {

    private final CelRuntime celRuntime;
    private final List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers;
    private final boolean metricsEnabled;
    private final List<String> globalParamNames;

    public CelRuleModule(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, boolean metricsEnabled, @Nullable List<String> globalParamNames) {
        this.celRuntime = celRuntime;
        this.celCompilerBuilderCustomizers = celCompilerBuilderCustomizers;
        this.metricsEnabled = metricsEnabled;
        this.globalParamNames = globalParamNames;
    }

    @Override
    public RuleFactory ruleFactory() {
        return RuleFactory.createCelRuleFactory(celRuntime, celCompilerBuilderCustomizers, metricsEnabled, globalParamNames);
    }

    @Override
    public ParameterExtractorFactory parameterExtractorFactory() {
        return ParameterExtractorFactory.createCelParameterExtractorFactory(celRuntime, celCompilerBuilderCustomizers);
    }

}
