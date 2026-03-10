package org.acme.rules.compiler.factory.rule;

import dev.cel.runtime.CelRuntime;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.RuleCompiler;
import org.acme.rules.compiler.factory.RuleFactory;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.CelRule;
import org.acme.rules.rule.NoopRule;
import org.acme.rules.rule.Rule;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Slf4j
public class CelRuleFactory implements RuleFactory {

    private final CelRuntime celRuntime;
    private final List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers;
    private final boolean metricsEnabled;
    private final List<String> globalParamNames;

    public CelRuleFactory(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, boolean metricsEnabled, @Nullable List<String> globalParamNames) {
        this.celRuntime = celRuntime;
        this.celCompilerBuilderCustomizers = celCompilerBuilderCustomizers;
        this.metricsEnabled = metricsEnabled;
        this.globalParamNames = globalParamNames;
    }


    @Override
    public boolean supports(@NonNull RuleDescriptor ruleDescriptor) {
        return ruleDescriptor instanceof ExpressionRuleDescriptor expressionRuleDescriptor
                && expressionRuleDescriptor.getRuleExpressionType().equals(RuleExpressionType.CEL);
    }

    @Override
    public Rule create(@NonNull RuleDescriptor ruleDescriptor, @NonNull RuleCompiler ruleCompiler, @Nullable ParameterExtractor parameterExtractor) {
        try {
            return CelRule.builder()
                    .ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor)
                    .celRuntime(celRuntime)
                    .celCompilerBuilderCustomizers(celCompilerBuilderCustomizers)
                    .parameterExtractor(parameterExtractor)
                    .metricsEnabled(metricsEnabled)
                    .globalParamNames(globalParamNames)
                    .build();
        } catch (Exception ex) {
            log.error("Failed to compile expression rule with specifications: [{}]. It will be replaced with a NOOP rule. Stacktrace: ", ruleDescriptor, ex);
            return NoopRule.builder()
                    .ruleDescriptor((ExpressionRuleDescriptor) ruleDescriptor)
                    .build();
        }
    }
}
