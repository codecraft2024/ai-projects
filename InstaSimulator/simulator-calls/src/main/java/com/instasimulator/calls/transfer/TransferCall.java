package com.instasimulator.calls.transfer;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.AccountInfo;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.exception.CallValidationException;
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
 * Transfers money from the primary account to a beneficiary.
 */
@Component
public class TransferCall extends AbstractApiCall {

    public TransferCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "transfer";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        BigDecimal amount = context.get("transferAmount", BigDecimal.class)
                .orElseGet(() -> RandomDataGenerator.amount(1, 50));
        String toAccount = context.get("toAccount", String.class).orElse("beneficiary-001");
        String fromAccount = context.primaryAccount().map(AccountInfo::getAccountId).orElse("primary");

        String body = JsonUtils.toJson(Map.of(
                "fromAccountId", fromAccount,
                "toAccountId", toAccount,
                "amount", amount,
                "currency", "USD",
                "note", "Simulated transfer"
        ));
        context.put("transferAmount", amount);
        return httpClient.post(url("/api/v1/transfers"), body, defaultHeaders(context));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Transfer failed");
        Map<String, Object> payload = JsonUtils.fromJson(result.getResponseBody(), Map.class);
        if (payload.get("transactionId") == null) {
            throw new CallValidationException(name(), "Missing transactionId");
        }
        context.put("lastTransferId", payload.get("transactionId"));
    }
}
