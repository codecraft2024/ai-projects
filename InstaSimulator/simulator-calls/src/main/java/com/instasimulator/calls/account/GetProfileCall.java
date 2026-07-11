package com.instasimulator.calls.account;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.dto.UserProfile;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

/**
 * Fetches the authenticated user profile.
 */
@Component
public class GetProfileCall extends AbstractApiCall {

    public GetProfileCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "get-profile";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        return httpClient.get(url(properties.getAuth().getProfilePath()), defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Get profile failed");
        UserProfile profile = JsonUtils.fromJson(result.getResponseBody(), UserProfile.class);
        context.setProfile(profile);
        if (profile.getUserId() != null) {
            context.setUserId(profile.getUserId());
        }
    }
}
