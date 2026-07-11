package com.instasimulator.common.enums;

/**
 * Outcome of a single API call or scenario step.
 */
public enum ExecutionStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    RETRYING,
    SKIPPED,
    TIMEOUT,
    CANCELLED
}
