package org.acme.rules.compiler.factory;

import lombok.extern.slf4j.Slf4j;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.rule.CelRule;
import org.acme.rules.rule.NoopRule;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;

import java.util.List;

@Slf4j
public class CelRuleFactory implements RuleFactory {

    private final boolean metricsEnabled;
    private final List<String> globalParamNames;
    private final List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers;

    public CelRuleFactory(boolean metricsEnabled, List<String> globalParamNames, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        this.metricsEnabled = metricsEnabled;
        this.globalParamNames = globalParamNames;
        this.celCompilerBuilderCustomizers = celCompilerBuilderCustomizers;
    }


    @Override
    public boolean supports(RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof ExpressionRuleDescriptor expressionRuleDescriptor
                && expressionRuleDescriptor.getRuleExpressionType().equals(RuleExpressionType.CEL);
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler) {
        try {
            return CelRule.builder()
                    .ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor)
                    .metricsEnabled(metricsEnabled)
                    .globalParamNames(globalParamNames)
                    .celCompilerBuilderCustomizers(celCompilerBuilderCustomizers)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to compile expression rule with specifications: [{}]. It will be replaced with a NOOP rule. Stacktrace: ", ruleDescriptor, ex);
            return NoopRule.builder()
                    .ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor)
                    .build();
        }
    }
}
