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

import java.math.BigDecimal;
import java.util.Map;

/**
 * Pays a utility bill.
 */
@Component
public class BillPaymentCall extends AbstractApiCall {

    public BillPaymentCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "bill-payment";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        BigDecimal amount = RandomDataGenerator.amount(10, 200);
        String body = JsonUtils.toJson(Map.of(
                "billerId", "ELEC-001",
                "amount", amount,
                "reference", "BILL-" + System.currentTimeMillis()
        ));
        return httpClient.post(url("/api/v1/payments/bills"), body, defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Bill payment failed");
        context.put("lastBillPayment", result.getResponseBody());
    }
}
