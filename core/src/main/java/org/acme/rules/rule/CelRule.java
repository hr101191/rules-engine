package org.acme.rules.rule;

import com.google.protobuf.Timestamp;
import dev.cel.common.CelOptions;
import dev.cel.common.CelValidationException;
import dev.cel.common.types.SimpleType;
import dev.cel.compiler.CelCompiler;
import dev.cel.compiler.CelCompilerBuilder;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.extensions.CelExtensions;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.cel.extension.ExtendedCelExtensions;
import org.acme.rules.model.ExpressionRuleDescriptor;
import org.acme.rules.model.ScopedParameter;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.*;

@Slf4j
public final class CelRule extends AbstractExpressionRule {


    private final ParameterExtractor parameterExtractor;
    private final CelRuntime.Program celProgram;

    @Builder
    public CelRule(@NonNull ExpressionRuleDescriptor ruleDescriptor, boolean metricsEnabled, @NonNull CelRuntime celRuntime, ParameterExtractor parameterExtractor, List<String> globalParamNames, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) throws CelValidationException, CelEvaluationException {
        super(ruleDescriptor, metricsEnabled);
        this.parameterExtractor = parameterExtractor;
        Objects.requireNonNull(celRuntime);
        CelCompilerBuilder celCompilerBuilder = CelCompilerFactory.standardCelCompilerBuilder()
                .addLibraries(
                        CelExtensions.optional(),
                        CelExtensions.strings(),
                        CelExtensions.protos(),
                        CelExtensions.math(CelOptions.DEFAULT),
                        CelExtensions.bindings(),
                        CelExtensions.encoders(CelOptions.DEFAULT),
                        CelExtensions.sets(CelOptions.DEFAULT),
                        CelExtensions.lists(),
                        CelExtensions.regex(),
                        CelExtensions.comprehensions(),
                        ExtendedCelExtensions.list(),
                        ExtendedCelExtensions.timestamp()
                )
                .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
                .addVar("root", SimpleType.DYN)
                .addVar("now", SimpleType.TIMESTAMP);
        if (globalParamNames != null) {
            globalParamNames.forEach(paramName -> celCompilerBuilder.addVar(paramName, SimpleType.DYN));
        }
        ScopedParametersDescriptor scopedParametersDescriptor = ruleDescriptor.getLocalParametersDescriptor();
        if (scopedParametersDescriptor != null) {
            for (ScopedParameter scopedParameter : scopedParametersDescriptor.getParameters()) {
                celCompilerBuilder.addVar(scopedParameter.getName(), SimpleType.DYN);
            }
        }
        if (celCompilerBuilderCustomizers != null) {
            celCompilerBuilderCustomizers.forEach(celCompilerBuilderCustomizer -> celCompilerBuilderCustomizer.customize(celCompilerBuilder));
        }
        CelCompiler celCompiler = celCompilerBuilder.build();
        this.celProgram = celRuntime.createProgram(celCompiler.compile(ruleDescriptor.getExpression()).getAst());
    }

    @Override
    protected boolean executeInternal(Map<String, Object> input, Map<String, Object> globalParams) throws Exception {
        log.info("Executing rule: {}", super.getRuleName());
        Map<String, Object> contextMap = new HashMap<>();
        contextMap.putAll(input);
        contextMap.putAll(globalParams);
        if (parameterExtractor != null) {
            contextMap.putAll(parameterExtractor.extract(input, globalParams));
        }
        Instant now = Instant.now();
        contextMap.put("now", Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build());
        log.info("Rule name: {} - Executing CEL Expression: {}", super.getRuleName(), super.getRuleDescriptor().getExpression());
        Object result = celProgram.eval(contextMap);
        log.info("Rule name: {} - Successfully executed CEL Expression: {} | Result: {}", super.getRuleName(), super.getRuleDescriptor().getExpression(), result);
        return result instanceof Boolean booleanResult ? booleanResult : false;
    }

}
