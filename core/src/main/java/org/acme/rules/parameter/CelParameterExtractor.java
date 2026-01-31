package org.acme.rules.parameter;

import com.google.protobuf.Timestamp;
import dev.cel.common.CelAbstractSyntaxTree;
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
import org.acme.rules.model.ScopedParameter;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.*;

@Slf4j
public class CelParameterExtractor implements ParameterExtractor {

    private final Map<String, CelRuntime.Program> paramExtractors;
    private final List<ScopedParameter> paramSpecifications;

    @Builder
    public CelParameterExtractor(@NonNull ScopedParametersDescriptor scopedParametersDescriptor, @NonNull CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) throws CelValidationException, CelEvaluationException {
        Objects.requireNonNull(scopedParametersDescriptor);
        Objects.requireNonNull(celRuntime);
        this.paramSpecifications = scopedParametersDescriptor.getParameters() != null ? scopedParametersDescriptor.getParameters() : Collections.emptyList();
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
                        CelExtensions.comprehensions()
                )
                .setStandardMacros(CelStandardMacro.STANDARD_MACROS)
                .addVar("root", SimpleType.DYN)
                .addVar("now", SimpleType.TIMESTAMP);
        for (ScopedParameter scopedParameter : this.paramSpecifications) {
            celCompilerBuilder.addVar(scopedParameter.getName(), SimpleType.DYN);
        }
        if (celCompilerBuilderCustomizers != null) {
            celCompilerBuilderCustomizers.forEach(celCompilerBuilderCustomizer -> celCompilerBuilderCustomizer.customize(celCompilerBuilder));
        }
        CelCompiler celCompiler = celCompilerBuilder.build();
        this.paramExtractors = new LinkedHashMap<>();
        for (ScopedParameter scopedParameter : this.paramSpecifications) {
            CelAbstractSyntaxTree celAbstractSyntaxTree = celCompiler.compile(scopedParameter.getExpression()).getAst();
            this.paramExtractors.put(scopedParameter.getName(), celRuntime.createProgram(celAbstractSyntaxTree));
        }
    }

    @Override
    public Map<String, Object> extract(Map<String, Object> input, Map<String, Object> globalParams) {
        Map<String, Object> contextMap = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String currentExpression = null;
        try {
            contextMap.putAll(input);
            contextMap.putAll(globalParams);
            Instant now = Instant.now();
            contextMap.put("now", Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build());
            for (ScopedParameter scopedParameter : paramSpecifications) {
                currentExpression = scopedParameter.getExpression();
                Object localResult = paramExtractors.get(scopedParameter.getName()).eval(contextMap);
                result.put(scopedParameter.getName(), localResult);
            }
        } catch (Exception ex) {
            log.error("Failed to extract parameter with expression: {} | Stacktrace: ", currentExpression, ex);
        }
        return result;
    }

}
