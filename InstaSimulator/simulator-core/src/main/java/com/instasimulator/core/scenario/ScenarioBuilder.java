package com.instasimulator.core.scenario;

import com.instasimulator.core.call.ApiCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fluent DSL for composing scenarios from calls.
 *
 * <pre>
 * ScenarioBuilder.create("transfer")
 *     .step(loginCall)
 *     .step(transferCall)
 *     .step(logoutCall)
 *     .build();
 * </pre>
 */
public final class ScenarioBuilder {

    private final String name;
    private String description = "";
    private final List<ApiCall> steps = new ArrayList<>();

    private ScenarioBuilder(String name) {
        this.name = name;
    }

    public static ScenarioBuilder create(String name) {
        return new ScenarioBuilder(name);
    }

    public ScenarioBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ScenarioBuilder step(ApiCall call) {
        steps.add(call);
        return this;
    }

    public BusinessScenario build() {
        List<ApiCall> immutableSteps = Collections.unmodifiableList(new ArrayList<>(steps));
        String scenarioName = name;
        String scenarioDescription = description;

        return new BusinessScenario() {
            @Override
            public String name() {
                return scenarioName;
            }

            @Override
            public String description() {
                return scenarioDescription;
            }

            @Override
            public List<ApiCall> steps() {
                return immutableSteps;
            }
        };
    }
}
