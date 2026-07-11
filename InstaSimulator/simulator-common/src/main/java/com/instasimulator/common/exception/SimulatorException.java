package com.instasimulator.common.exception;

/**
 * Base unchecked exception for the simulation framework.
 */
public class SimulatorException extends RuntimeException {

    public SimulatorException(String message) {
        super(message);
    }

    public SimulatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
