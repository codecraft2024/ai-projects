package com.instasimulator.common.exception;

/**
 * Thrown when an API call validation fails.
 */
public class CallValidationException extends SimulatorException {

    private final String callName;

    public CallValidationException(String callName, String message) {
        super("[%s] validation failed: %s".formatted(callName, message));
        this.callName = callName;
    }

    public String getCallName() {
        return callName;
    }
}
