package com.instasimulator.core.call;

import com.instasimulator.common.constants.SimulatorConstants;
import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.enums.ExecutionStatus;
import com.instasimulator.common.exception.CallValidationException;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.config.properties.SimulatorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Template method base for API calls providing timing, logging, and auth headers.
 * Prefer composition via {@link SimulatorHttpClient}; subclass only for call-specific logic.
 */
public abstract class AbstractApiCall implements ApiCall {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final SimulatorHttpClient httpClient;
    protected final SimulatorProperties properties;

    protected AbstractApiCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        this.httpClient = httpClient;
        this.properties = properties;
    }

    @Override
    public final CallResult execute(SimulationContext context) {
        Instant startedAt = Instant.now();
        int retries = 0;
        CallResult lastResult = null;

        while (true) {
            try {
                log.info("[{}][{}] executing call={} attempt={}",
                        context.getCurrentScenario(), context.getCorrelationId(), name(), retries + 1);

                HttpResponse response = doExecute(context);
                CallResult result = toResult(context, response, startedAt, retries);
                if (!response.isSuccessful()) {
                    throw new CallValidationException(name(),
                            "HTTP " + response.getStatusCode() + ": " + truncate(response.getBody()));
                }
                validate(context, result);

                result.setStatus(ExecutionStatus.SUCCESS);
                log.info("[{}][{}] call={} status=SUCCESS durationMs={} retries={}",
                        context.getCurrentScenario(), context.getCorrelationId(),
                        name(), result.getDurationMs(), retries);
                return result;
            } catch (Exception ex) {
                lastResult = CallResult.builder()
                        .callName(name())
                        .status(ExecutionStatus.FAILED)
                        .correlationId(context.getCorrelationId())
                        .retryCount(retries)
                        .startedAt(startedAt)
                        .finishedAt(Instant.now())
                        .durationMs(System.currentTimeMillis() - startedAt.toEpochMilli())
                        .errorMessage(ex.getMessage())
                        .statusCode(ex instanceof CallValidationException ? 422 : 500)
                        .build();

                if (!shouldRetry(lastResult, retries + 1)) {
                    log.error("[{}][{}] call={} status=FAILED error={}",
                            context.getCurrentScenario(), context.getCorrelationId(),
                            name(), ex.getMessage());
                    return lastResult;
                }

                retries++;
                lastResult.setStatus(ExecutionStatus.RETRYING);
                log.warn("[{}][{}] call={} retrying attempt={}",
                        context.getCurrentScenario(), context.getCorrelationId(), name(), retries + 1);
                sleep(properties.getRetry().getBackoffMs() * retries);
            }
        }
    }

    /**
     * Perform the HTTP interaction and return the raw response.
     */
    protected abstract HttpResponse doExecute(SimulationContext context);

    protected Map<String, String> defaultHeaders(SimulationContext context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", SimulatorConstants.CONTENT_TYPE_JSON);
        headers.put("Content-Type", SimulatorConstants.CONTENT_TYPE_JSON);
        headers.put(SimulatorConstants.CORRELATION_ID_HEADER, context.getCorrelationId());
        headers.put(SimulatorConstants.SESSION_ID_HEADER, context.getSessionId());
        if (context.isAuthenticated()) {
            headers.put(SimulatorConstants.AUTHORIZATION_HEADER,
                    SimulatorConstants.BEARER_PREFIX + context.getAccessToken());
        }
        return headers;
    }

    protected String url(String path) {
        String base = properties.getBaseUrl();
        if (base.endsWith("/") && path.startsWith("/")) {
            return base.substring(0, base.length() - 1) + path;
        }
        return base + path;
    }

    protected void requireSuccess(CallResult result, String message) {
        if (!result.isSuccess() && result.getStatusCode() >= 400) {
            throw new CallValidationException(name(), message + " (status=" + result.getStatusCode() + ")");
        }
    }

    private CallResult toResult(SimulationContext context, HttpResponse response,
                                Instant startedAt, int retries) {
        return CallResult.builder()
                .callName(name())
                .status(response.isSuccessful() ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILED)
                .statusCode(response.getStatusCode())
                .durationMs(response.getDurationMs())
                .retryCount(retries)
                .correlationId(context.getCorrelationId())
                .requestSummary(name())
                .responseBody(response.getBody())
                .responseSummary(truncate(response.getBody()))
                .startedAt(startedAt)
                .finishedAt(Instant.now())
                .build();
    }

    private static String truncate(String body) {
        if (body == null) {
            return "";
        }
        return body.length() > 300 ? body.substring(0, 300) + "..." : body;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
