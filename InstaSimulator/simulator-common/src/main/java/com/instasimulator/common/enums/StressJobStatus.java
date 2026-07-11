package com.instasimulator.common.enums;

/**
 * High-level stress test lifecycle states.
 */
public enum StressJobStatus {
    IDLE,
    RAMPING_UP,
    RUNNING,
    RAMPING_DOWN,
    STOPPING,
    STOPPED,
    COMPLETED,
    FAILED
}
