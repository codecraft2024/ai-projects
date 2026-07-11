package com.instasimulator.common.http;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * HTTP response returned by {@link SimulatorHttpClient}.
 */
@Data
@Builder
public class HttpResponse {

    private int statusCode;
    private String body;
    private Map<String, String> headers;
    private long durationMs;

    public Map<String, String> getHeaders() {
        return headers == null ? Collections.emptyMap() : headers;
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}
