package com.instasimulator.calls.account;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.AccountInfo;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.http.HttpResponse;
import com.instasimulator.common.http.SimulatorHttpClient;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.call.AbstractApiCall;
import org.springframework.stereotype.Component;

/**
 * Retrieves recent transactions for the primary account.
 */
@Component
public class GetTransactionsCall extends AbstractApiCall {

    public GetTransactionsCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "get-transactions";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        String accountId = context.primaryAccount()
                .map(AccountInfo::getAccountId)
                .orElse("primary");
        return httpClient.get(url("/api/v1/accounts/" + accountId + "/transactions"), defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Get transactions failed");
        context.put("lastTransactions", result.getResponseBody());
    }
}
