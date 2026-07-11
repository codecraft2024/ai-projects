package com.instasimulator.core.call;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;

/**
 * Contract for every independent API call in the simulator.
 */
public interface ApiCall {

    /**
     * Unique call name used in logs, metrics, and scenario DSLs.
     */
    String name();

    /**
     * Execute the call against the remote system using the shared context.
     */
    CallResult execute(SimulationContext context);

    /**
     * Validate the outcome and update context state as needed.
     */
    void validate(SimulationContext context, CallResult result);

    /**
     * Whether this call should be retried for the given failure.
     */
    default boolean shouldRetry(CallResult result, int attempt) {
        return !result.isSuccess() && attempt < 3
                && (result.getStatusCode() >= 500 || result.getStatusCode() == 429);
    }
}
