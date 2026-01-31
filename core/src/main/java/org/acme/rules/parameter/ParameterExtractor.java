package org.acme.rules.parameter;

import dev.cel.runtime.CelRuntime;
import org.acme.rules.cel.CelCompilerBuilderCustomizer;
import org.acme.rules.model.ScopedParametersDescriptor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public interface ParameterExtractor {

    Logger log = LoggerFactory.getLogger(ParameterExtractor.class);

    static ParameterExtractor buildParameterExtractor(@NonNull ScopedParametersDescriptor scopedParametersDescriptor, @NonNull CelRuntime celRuntime, List<CelCompilerBuilderCustomizer> celCompilerBuilderCustomizerList) {
        return switch (scopedParametersDescriptor.getExpressionType()) {
            case CEL -> {
                try {
                    yield CelParameterExtractor.builder().scopedParametersDescriptor(scopedParametersDescriptor).celRuntime(celRuntime).celCompilerBuilderCustomizers(celCompilerBuilderCustomizerList).build();
                } catch (Exception ex) {
                    log.error("Failed to create parameter extractor with specifications: [{}]. It will be replaced with a NOOP parameter extractor. Stacktrace: ", scopedParametersDescriptor, ex);
                    yield new NoopParameterExtractor();
                }
            }
            case JS -> {
                try {
                    yield JsParameterExtractor.builder().scopedParametersDescriptor(scopedParametersDescriptor).build();
                } catch (Exception ex) {
                    log.error("Failed to create parameter extractor with specifications: [{}]. It will be replaced with a NOOP parameter extractor. Stacktrace: ", scopedParametersDescriptor, ex);
                    yield new NoopParameterExtractor();
                }
            }
        };
    }

    Map<String, Object> extract(Map<String, Object> input, Map<String, Object> globalParams);
}
