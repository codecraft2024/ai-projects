package com.instasimulator.stress.runner;

import lombok.Builder;
import lombok.Data;

/**
 * Parameters for a stress / load test run.
 */
@Data
@Builder
public class StressConfig {

    private String scenarioName;

    @Builder.Default
    private int virtualUsers = 10;

    @Builder.Default
    private long durationSeconds = 60;

    @Builder.Default
    private long rampUpSeconds = 10;

    @Builder.Default
    private long rampDownSeconds = 5;

    @Builder.Default
    private long thinkTimeMinMs = 200;

    @Builder.Default
    private long thinkTimeMaxMs = 1500;

    @Builder.Default
    private int loopsPerUser = 0;

    public static StressConfig users(int count) {
        return StressConfig.builder().virtualUsers(count).build();
    }
}
