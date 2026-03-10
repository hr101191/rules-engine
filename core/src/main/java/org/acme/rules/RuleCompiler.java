package org.acme.rules;

import dev.cel.runtime.CelRuntime;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.model.CompositeRuleDescriptor;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.RuleDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.acme.rules.rule.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RuleCompiler {

    public static final RuleCompiler INSTANCE = new RuleCompiler();

    public Rule compile(RuleDescriptor ruleDescriptor, CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, List<String> globalParamNames) {
        return compileInternal(ruleDescriptor, celRuntime, celCompilerBuilderCustomizers, globalParamNames);
    }

    private Rule compileInternal(RuleDescriptor ruleDescriptor, CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, List<String> globalParamNames) {
        return switch (ruleDescriptor) {
            case ExpressionRuleDescriptor expressionRuleDescriptor -> compileExpressionRule(expressionRuleDescriptor, celRuntime, celCompilerBuilderCustomizers, globalParamNames);
            case CompositeRuleDescriptor compositeRuleDescriptor -> compileCompositeRule(compositeRuleDescriptor, celRuntime, celCompilerBuilderCustomizers, globalParamNames);
            default -> throw new IllegalArgumentException("Unknown rule descriptor type: " + ruleDescriptor.getClass().getName());
        };
    }

    private Rule compileExpressionRule(ExpressionRuleDescriptor expressionRuleDescriptor, CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, List<String> globalParamNames) {
        return switch (expressionRuleDescriptor.getRuleExpressionType()) {
            case CEL -> {
                try {
                    CelRule.CelRuleBuilder builder = CelRule.builder()
                            .ruleDescriptor(expressionRuleDescriptor)
                            .metricsEnabled(true)
                            .celRuntime(celRuntime)
                            .celCompilerBuilderCustomizers(celCompilerBuilderCustomizers)
                            .globalParamNames(globalParamNames);
                    if (expressionRuleDescriptor.getLocalParametersDescriptor() != null) {
                        builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(expressionRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                    }
                    yield builder.build();
                } catch (Exception ex) {
                    log.error("Failed to compile expression rule with specifications: [{}]. It will be replaced with a NOOP rule. Stacktrace: ", expressionRuleDescriptor, ex);
                    yield NoopRule.builder().ruleDescriptor(expressionRuleDescriptor).metricsEnabled(true).build();
                }
            }
            case JS -> {
                try {
                    JsRule.JsRuleBuilder builder = JsRule.builder()
                            .ruleDescriptor(expressionRuleDescriptor)
                            .metricsEnabled(true);
                    if (expressionRuleDescriptor.getLocalParametersDescriptor() != null) {
                        builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(expressionRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                    }
                    yield builder.build();
                } catch (Exception ex) {
                    log.error("Failed to compile expression rule with specifications: [{}]. It will be replaced with a NOOP rule. Stacktrace: ", expressionRuleDescriptor, ex);
                    yield NoopRule.builder().ruleDescriptor(expressionRuleDescriptor).metricsEnabled(true).build();
                }
            }
        };
    }

    private Rule compileCompositeRule(CompositeRuleDescriptor compositeRuleDescriptor, CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers, List<String> globalParamNames) {
        List<Rule> children = new ArrayList<>();
        for (RuleDescriptor ruleDescriptor : compositeRuleDescriptor.getRules()) {
            Rule child = compileInternal(ruleDescriptor, celRuntime, celCompilerBuilderCustomizers, globalParamNames);
            children.add(child);
        }
        return switch (compositeRuleDescriptor.getOperator()) {
            case AND -> {
                AndRule.AndRuleBuilder builder = AndRule.builder()
                        .ruleDescriptor(compositeRuleDescriptor)
                        .rules(children)
                        .metricsEnabled(true);
                if (compositeRuleDescriptor.getLocalParametersDescriptor() != null) {
                    builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(compositeRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                }
                yield builder.build();
            }
            case AND_ALSO -> {
                AndAlsoRule.AndAlsoRuleBuilder builder = AndAlsoRule.builder()
                        .ruleDescriptor(compositeRuleDescriptor)
                        .rules(children)
                        .metricsEnabled(true);
                if (compositeRuleDescriptor.getLocalParametersDescriptor() != null) {
                    builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(compositeRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                }
                yield builder.build();
            }
            case OR -> {
                OrRule.OrRuleBuilder builder = OrRule.builder()
                        .ruleDescriptor(compositeRuleDescriptor)
                        .rules(children)
                        .metricsEnabled(true);
                if (compositeRuleDescriptor.getLocalParametersDescriptor() != null) {
                    builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(compositeRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                }
                yield builder.build();
            }
            case OR_ELSE -> {
                OrElseRule.OrElseRuleBuilder builder = OrElseRule.builder()
                        .ruleDescriptor(compositeRuleDescriptor)
                        .rules(children)
                        .metricsEnabled(true);
                if (compositeRuleDescriptor.getLocalParametersDescriptor() != null) {
                    builder.parameterExtractor(ParameterExtractor.buildParameterExtractor(compositeRuleDescriptor.getLocalParametersDescriptor(), celRuntime, celCompilerBuilderCustomizers));
                }
                yield builder.build();
            }
        };
    }

}
