package org.acme.rules.compiler.factory;

import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

@VertxGen
public interface RuleFactory {

    static RuleFactory createCompositeRuleFactory() {
        return new CompositeRuleFactory();
    }

    boolean supports(RuleDescriptor ruleDescriptor);

    Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler);
}
