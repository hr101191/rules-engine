package org.acme.rules.compiler.factory.parameter;

import dev.cel.runtime.CelRuntime;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.ParameterExtractorFactory;
import org.acme.rules.model.RuleExpressionType;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.acme.rules.parameter.CelParameterExtractor;
import org.acme.rules.parameter.NoopParameterExtractor;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CelParameterExtractorFactory implements ParameterExtractorFactory {

    private final CelRuntime celRuntime;
    private final List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers;

    public CelParameterExtractorFactory(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        this.celRuntime = celRuntime;
        this.celCompilerBuilderCustomizers = celCompilerBuilderCustomizers;
    }

    @Override
    public boolean supports(@NonNull ScopedParametersDescriptor scopedParametersDescriptor) {
        return scopedParametersDescriptor.getExpressionType().equals(RuleExpressionType.CEL);
    }

    @Override
    public ParameterExtractor create(@NonNull ScopedParametersDescriptor scopedParametersDescriptor) {
        try {
            return CelParameterExtractor.builder().scopedParametersDescriptor(scopedParametersDescriptor)
                    .celRuntime(celRuntime)
                    .celCompilerBuilderCustomizers(celCompilerBuilderCustomizers)
                    .build();
        } catch (Exception x) {
            return new NoopParameterExtractor();
        }
    }

}
