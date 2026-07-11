package com.instasimulator.common.exception;

/**
 * Thrown when HTTP communication fails after retries are exhausted.
 */
public class HttpClientException extends SimulatorException {

    private final int statusCode;

    public HttpClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
