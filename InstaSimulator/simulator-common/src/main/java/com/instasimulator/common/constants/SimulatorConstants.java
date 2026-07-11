package com.instasimulator.common.constants;

/**
 * Shared framework constants.
 */
public final class SimulatorConstants {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String SESSION_ID_HEADER = "X-Session-Id";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_USER_AGENT = "InstaSimulator/1.0";

    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 5_000;
    public static final int DEFAULT_READ_TIMEOUT_MS = 30_000;
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final long DEFAULT_RETRY_BACKOFF_MS = 500L;

    public static final String ATTR_REQUEST_BODY = "requestBody";
    public static final String ATTR_RESPONSE_BODY = "responseBody";
    public static final String ATTR_STATUS_CODE = "statusCode";
    public static final String ATTR_DURATION_MS = "durationMs";

    private SimulatorConstants() {
    }
}
