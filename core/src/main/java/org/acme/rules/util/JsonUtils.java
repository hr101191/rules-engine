package org.acme.rules.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonUtils {

    public static final ObjectMapper CEL_OBJECT_MAPPER = JsonMapper.builder()
            .enable(DeserializationFeature.USE_LONG_FOR_INTS)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .build();

}
