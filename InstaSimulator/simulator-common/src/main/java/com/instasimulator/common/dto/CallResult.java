package com.instasimulator.common.dto;

import com.instasimulator.common.enums.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Result of a single API call execution.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallResult {

    private String callName;
    private ExecutionStatus status;
    private int statusCode;
    private long durationMs;
    private int retryCount;
    private String correlationId;
    private String requestSummary;
    private String responseSummary;
    private String responseBody;
    private String errorMessage;
    private Instant startedAt;
    private Instant finishedAt;

    public boolean isSuccess() {
        return status == ExecutionStatus.SUCCESS;
    }
}
