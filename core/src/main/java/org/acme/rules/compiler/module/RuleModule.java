package org.acme.rules.compiler.module;

import dev.cel.runtime.CelRuntime;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.compiler.factory.RuleFactory;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@VertxGen
public interface RuleModule {

    @GenIgnore
    static RuleModule createCelRuleModule(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, boolean metricsEnabled, @Nullable List<String> globalParamNames) {
        return new CelRuleModule(celRuntime, celCompilerBuilderCustomizers, metricsEnabled, globalParamNames);
    }

    @GenIgnore
    static RuleModule createJsRuleModule() {
        return new JsRuleModule();
    }

    @GenIgnore
    static RuleModule createCompositeRuleModule() {
        return new CompositeRuleModule();
    }

    RuleFactory ruleFactory();

    @Nullable ParameterExtractorFactory parameterExtractorFactory();

}
