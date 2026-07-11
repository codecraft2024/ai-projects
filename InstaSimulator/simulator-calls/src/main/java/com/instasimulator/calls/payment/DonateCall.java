package com.instasimulator.calls.payment;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.common.util.RandomDataGenerator;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Donates to a charity organization.
 */
@Component
public class DonateCall extends AbstractApiCall {

    public DonateCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "donate";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String body = JsonUtils.toJson(Map.of(
                "charityId", "CHARITY-100",
                "amount", RandomDataGenerator.amount(5, 100)
        ));
        return httpClient.post(url("/api/v1/payments/donate"), body, defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Donation failed");
        context.put("lastDonation", result.getResponseBody());
    }
}
