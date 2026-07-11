package com.instasimulator.common.http;

import lombok.Builder;
import lombok.Data;

/**
 * Connection pool and timeout settings for the HTTP client.
 */
@Data
@Builder
public class HttpClientConfig {

    @Builder.Default
    private int connectTimeoutMs = 5_000;

    @Builder.Default
    private int responseTimeoutMs = 30_000;

    @Builder.Default
    private int maxConnections = 200;

    @Builder.Default
    private int maxConnectionsPerRoute = 50;

    @Builder.Default
    private boolean logRequests = true;

    @Builder.Default
    private boolean logResponses = true;

    @Builder.Default
    private int maxRetries = 3;

    @Builder.Default
    private long retryBackoffMs = 500L;

    public static HttpClientConfig defaults() {
        return HttpClientConfig.builder().build();
    }
}
