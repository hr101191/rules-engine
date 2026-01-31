package org.acme.rules.parameter;

import lombok.Builder;
import org.acme.rules.model.ScopedParametersDescriptor;

import java.util.HashMap;
import java.util.Map;

public class JsParameterExtractor implements ParameterExtractor {

    private final ScopedParametersDescriptor scopedParametersDescriptor;

    @Builder
    public JsParameterExtractor(ScopedParametersDescriptor scopedParametersDescriptor) {
        this.scopedParametersDescriptor = scopedParametersDescriptor;
    }

    @Override
    public Map<String, Object> extract(Map<String, Object> input, Map<String, Object> globalParams) {
        return new HashMap<>();
    }

}
