package com.instasimulator.calls.account;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.AccountInfo;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.exception.CallValidationException;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.common.util.JsonUtils;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Fetches balance for the primary account.
 */
@Component
public class GetBalanceCall extends AbstractApiCall {

    public GetBalanceCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "get-balance";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String accountId = context.primaryAccount()
                .map(AccountInfo::getAccountId)
                .orElse("primary");
        return httpClient.get(url("/api/v1/accounts/" + accountId + "/balance"), defaultHeaders(context));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Get balance failed");
        Map<String, Object> payload = JsonUtils.fromJson(result.getResponseBody(), Map.class);
        Object balance = payload.get("balance");
        if (balance == null) {
            throw new CallValidationException(name(), "Missing balance");
        }
        BigDecimal amount = new BigDecimal(String.valueOf(balance));
        context.primaryAccount().ifPresent(account -> account.setBalance(amount));
        context.put("lastBalance", amount);
    }
}
