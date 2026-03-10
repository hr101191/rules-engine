package org.acme.rules.compiler.factory;

import dev.cel.runtime.CelRuntime;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.compiler.factory.rule.CelRuleFactory;
import org.acme.rules.compiler.factory.rule.CompositeRuleFactory;
import org.acme.rules.compiler.factory.rule.JsRuleFactory;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@VertxGen
public interface RuleFactory {

    @GenIgnore
    static RuleFactory createCelRuleFactory(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, boolean metricsEnabled, @Nullable List<String> globalParamNames) {
        return new CelRuleFactory(celRuntime, celCompilerBuilderCustomizers, metricsEnabled, globalParamNames);
    }

    @GenIgnore
    static RuleFactory createJsRuleFactory() {
        return new JsRuleFactory();
    }

    static RuleFactory createCompositeRuleFactory() {
        return new CompositeRuleFactory();
    }

    boolean supports(@NonNull RuleDescriptor ruleDescriptor);

    Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler, @Nullable ParameterExtractor parameterExtractor);

}
