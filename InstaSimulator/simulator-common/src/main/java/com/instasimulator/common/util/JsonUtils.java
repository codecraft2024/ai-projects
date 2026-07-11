package com.instasimulator.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Shared Jackson mappers for JSON and YAML.
 */
public final class JsonUtils {

    private static final ObjectMapper JSON_MAPPER = createJsonMapper();
    private static final ObjectMapper YAML_MAPPER = createYamlMapper();

    private JsonUtils() {
    }

    public static ObjectMapper jsonMapper() {
        return JSON_MAPPER;
    }

    public static ObjectMapper yamlMapper() {
        return YAML_MAPPER;
    }

    public static String toJson(Object value) {
        try {
            return JSON_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return JSON_MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize JSON", e);
        }
    }

    private static ObjectMapper createJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    private static ObjectMapper createYamlMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
