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
 * Authenticates a user and stores access/refresh tokens in context.
 */
@Component
public class LoginCall extends AbstractApiCall {

    public LoginCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String body = JsonUtils.toJson(Map.of(
                "username", context.getUsername(),
                "password", context.getPassword()
        ));
        return httpClient.post(url(properties.getAuth().getLoginPath()), body, defaultHeaders(context));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Login failed");
        Map<String, Object> payload = JsonUtils.fromJson(result.getResponseBody(), Map.class);
        Object accessToken = payload.get("accessToken");
        Object refreshToken = payload.get("refreshToken");
        if (accessToken == null) {
            throw new CallValidationException(name(), "Missing access token");
        }
        context.setAccessToken(String.valueOf(accessToken));
        if (refreshToken != null) {
            context.setRefreshToken(String.valueOf(refreshToken));
        }
        if (payload.get("userId") != null) {
            context.setUserId(String.valueOf(payload.get("userId")));
        }
    }
}
