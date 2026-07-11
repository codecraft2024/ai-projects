package com.instasimulator.core.retry;

import lombok.Builder;
import lombok.Data;

/**
 * Retry policy applied by the execution engine.
 */
@Data
@Builder
public class RetryPolicy {

    @Builder.Default
    private int maxAttempts = 3;

    @Builder.Default
    private long backoffMs = 500L;

    @Builder.Default
    private boolean retryOnServerError = true;

    @Builder.Default
    private boolean retryOnTimeout = true;

    public static RetryPolicy defaults() {
        return RetryPolicy.builder().build();
    }
}
