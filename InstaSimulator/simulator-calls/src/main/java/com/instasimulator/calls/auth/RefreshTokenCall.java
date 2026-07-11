package com.instasimulator.calls.auth;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.exception.CallValidationException;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Refreshes an expired access token.
 */
@Component
public class RefreshTokenCall extends AbstractApiCall {

    public RefreshTokenCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "refresh-token";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String body = JsonUtils.toJson(Map.of("refreshToken", context.getRefreshToken()));
        return httpClient.post(url(properties.getAuth().getRefreshPath()), body, defaultHeaders(context));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Token refresh failed");
        Map<String, Object> payload = JsonUtils.fromJson(result.getResponseBody(), Map.class);
        Object accessToken = payload.get("accessToken");
        if (accessToken == null) {
            throw new CallValidationException(name(), "Missing refreshed access token");
        }
        context.setAccessToken(String.valueOf(accessToken));
    }
}
