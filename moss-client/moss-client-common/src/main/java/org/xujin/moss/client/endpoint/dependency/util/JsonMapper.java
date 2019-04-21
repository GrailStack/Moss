package org.xujin.moss.client.endpoint.dependency.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
    private static final JsonMapper instance = new JsonMapper();

    public static JsonMapper defaultMapper() {
        return instance;
    }

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(JsonInclude.Include include) {
        mapper = new ObjectMapper();

        if (include != null) {
            mapper.setSerializationInclusion(include);
        }

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

}
