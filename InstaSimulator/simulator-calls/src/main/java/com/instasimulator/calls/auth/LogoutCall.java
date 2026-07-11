package com.instasimulator.calls.auth;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

/**
 * Ends the authenticated session.
 */
@Component
public class LogoutCall extends AbstractApiCall {

    public LogoutCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "logout";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        return httpClient.post(url(properties.getAuth().getLogoutPath()), "{}", defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Logout failed");
        context.setAccessToken(null);
        context.setRefreshToken(null);
    }
}
