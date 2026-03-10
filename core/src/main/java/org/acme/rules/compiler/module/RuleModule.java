package org.acme.rules.compiler.module;

import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.RuleFactory;
import org.jspecify.annotations.Nullable;

import java.util.List;

@VertxGen
public interface RuleModule {

    static RuleModule createCelRuleModule(boolean metricsEnabled, @Nullable List<String> globalParamNames, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        return new CelRuleModule(metricsEnabled, globalParamNames, celCompilerBuilderCustomizers);
    }

    static RuleModule createJsRuleModule() {
        return new JsRuleModule();
    }

    static RuleModule createCompositeRuleModule() {
        return new CompositeRuleModule();
    }

    RuleFactory ruleFactory();
}
