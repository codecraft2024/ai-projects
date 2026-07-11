package com.instasimulator.reports.model;

import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.stress.stats.StressStatistics;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate report model for scenario and stress executions.
 */
@Data
@Builder
public class ExecutionReport {

    private String reportId;
    private Instant generatedAt;
    private String title;

    @Builder.Default
    private List<ScenarioResult> scenarioResults = new ArrayList<>();

    private StressStatistics.Snapshot stressSnapshot;

    private long totalScenarios;
    private long successfulScenarios;
    private long failedScenarios;
    private long averageDurationMs;
    private long p50;
    private long p90;
    private long p95;
    private long p99;
}
