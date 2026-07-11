package com.instasimulator.common.dto;

import com.instasimulator.common.enums.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate result of a full scenario execution.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioResult {

    private String executionId;
    private String scenarioName;
    private String correlationId;
    private String userId;
    private ExecutionStatus status;
    private long durationMs;
    private Instant startedAt;
    private Instant finishedAt;
    private String errorMessage;

    @Builder.Default
    private List<CallResult> callResults = new ArrayList<>();

    public boolean isSuccess() {
        return status == ExecutionStatus.SUCCESS;
    }

    public void addCallResult(CallResult result) {
        callResults.add(result);
    }
}
