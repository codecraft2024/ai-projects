package com.instasimulator.common.exception;

/**
 * Thrown when a scenario cannot complete successfully.
 */
public class ScenarioExecutionException extends SimulatorException {

    private final String scenarioName;

    public ScenarioExecutionException(String scenarioName, String message, Throwable cause) {
        super("[%s] %s".formatted(scenarioName, message), cause);
        this.scenarioName = scenarioName;
    }

    public ScenarioExecutionException(String scenarioName, String message) {
        this(scenarioName, message, null);
    }

    public String getScenarioName() {
        return scenarioName;
    }
}
