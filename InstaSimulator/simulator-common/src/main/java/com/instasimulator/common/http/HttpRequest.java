package com.instasimulator.common.http;

import com.instasimulator.common.enums.HttpMethod;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Collections;
import java.util.Map;

/**
 * Immutable HTTP request descriptor used by {@link SimulatorHttpClient}.
 */
@Data
@Builder
public class HttpRequest {

    private HttpMethod method;
    private String url;
    private String body;

    @Singular
    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers == null ? Collections.emptyMap() : headers;
    }
}
