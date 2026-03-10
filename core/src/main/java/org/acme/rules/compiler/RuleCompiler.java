package org.acme.rules.compiler;

import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.compiler.factory.RuleFactory;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@VertxGen
public interface RuleCompiler {

    static RuleCompiler create() {
        return new RuleCompilerImpl(new ArrayList<>());
    }

    static RuleCompiler create(@NonNull List<@NonNull RuleFactory> ruleFactories) {
        return new RuleCompilerImpl(ruleFactories);
    }

    Rule compile(@NonNull RuleDescriptor ruleDescriptor);

    RuleCompiler addRuleFactory(@NonNull RuleFactory ruleFactory);
}
