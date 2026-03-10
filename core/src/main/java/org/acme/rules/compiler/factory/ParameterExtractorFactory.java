package org.acme.rules.compiler.factory;

import dev.cel.runtime.CelRuntime;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.compiler.factory.parameter.CelParameterExtractorFactory;
import org.acme.rules.compiler.factory.parameter.JsParameterExtractorFactory;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.acme.rules.parameter.ParameterExtractor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

@VertxGen
public interface ParameterExtractorFactory {

    @GenIgnore
    static ParameterExtractorFactory createCelParameterExtractorFactory(@NonNull CelRuntime celRuntime, @Nullable List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizers) {
        return new CelParameterExtractorFactory(celRuntime, celCompilerBuilderCustomizers);
    }

    @GenIgnore
    static ParameterExtractorFactory createJsParameterExtractorFactory() {
        return new JsParameterExtractorFactory();
    }

    boolean supports(@NonNull ScopedParametersDescriptor scopedParametersDescriptor);

    ParameterExtractor create(@NonNull ScopedParametersDescriptor scopedParametersDescriptor);

}
