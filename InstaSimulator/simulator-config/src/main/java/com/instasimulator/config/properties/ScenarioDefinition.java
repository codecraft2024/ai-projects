package com.instasimulator.config.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * YAML-mapped scenario definition.
 */
@Data
public class ScenarioDefinition {

    private String name;
    private String description;
    private List<String> steps = new ArrayList<>();
    private long thinkTimeMs = 0L;
}
