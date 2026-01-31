package org.acme.rules.parameter;

import java.util.HashMap;
import java.util.Map;

public class NoopParameterExtractor implements ParameterExtractor {

    @Override
    public Map<String, Object> extract(Map<String, Object> input, Map<String, Object> globalParams) {
        return new HashMap<>();
    }

}
