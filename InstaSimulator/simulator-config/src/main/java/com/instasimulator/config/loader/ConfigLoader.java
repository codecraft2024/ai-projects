package com.instasimulator.config.loader;

import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.ScenarioDefinition;
import com.instasimulator.config.properties.UserCredentials;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Loads scenario and user-pool YAML resources from the classpath.
 */
@Component
public class ConfigLoader {

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public Optional<ScenarioDefinition> loadScenario(String name) {
        return loadScenarios().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<ScenarioDefinition> loadScenarios() {
        return loadYamlList("classpath*:scenarios/*.yml", ScenarioDefinition.class);
    }

    public List<UserCredentials> loadUserPool() {
        return loadYamlList("classpath*:users/*.yml", UserCredentials.class);
    }

    private <T> List<T> loadYamlList(String pattern, Class<T> type) {
        List<T> results = new ArrayList<>();
        try {
            Resource[] resources = resolver.getResources(pattern);
            for (Resource resource : resources) {
                try (InputStream in = resource.getInputStream()) {
                    T[] values = JsonUtils.yamlMapper().readValue(in,
                            JsonUtils.yamlMapper().getTypeFactory().constructArrayType(type));
                    results.addAll(Arrays.asList(values));
                } catch (Exception singleObject) {
                    try (InputStream in = resource.getInputStream()) {
                        results.add(JsonUtils.yamlMapper().readValue(in, type));
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config resources: " + pattern, e);
        }
        return results;
    }
}
