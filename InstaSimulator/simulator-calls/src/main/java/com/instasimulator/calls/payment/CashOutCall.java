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
 * Cashes out funds to an external wallet or ATM reference.
 */
@Component
public class CashOutCall extends AbstractApiCall {

    public CashOutCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "cash-out";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String body = JsonUtils.toJson(Map.of(
                "amount", RandomDataGenerator.amount(20, 150),
                "channel", "ATM"
        ));
        return httpClient.post(url("/api/v1/payments/cashout"), body, defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Cash-out failed");
        context.put("lastCashOut", result.getResponseBody());
    }
}
