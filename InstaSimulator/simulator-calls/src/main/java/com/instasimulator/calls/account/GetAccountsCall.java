package com.instasimulator.calls.account;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

/**
 * Retrieves accounts for the authenticated user.
 */
@Component
public class GetAccountsCall extends AbstractApiCall {

    public GetAccountsCall(SimulatorHttpClient httpClient, SimulatorProperties properties) {
        super(httpClient, properties);
    }

    @Override
    public String name() {
        return "get-accounts";
    }

    @Override
    protected HttpResponse doExecute(SimulationContext context) {
        return httpClient.get(url("/api/v1/accounts"), defaultHeaders(context));
    }

    @Override
    public void validate(SimulationContext context, CallResult result) {
        requireSuccess(result, "Get accounts failed");
        try {
            List<AccountInfo> accounts = JsonUtils.jsonMapper()
                    .readValue(result.getResponseBody(), new TypeReference<>() {
                    });
            if (accounts == null || accounts.isEmpty()) {
                throw new CallValidationException(name(), "No accounts returned");
            }
            context.setAccounts(accounts);
        } catch (CallValidationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new CallValidationException(name(), "Unable to parse accounts: " + ex.getMessage());
        }
    }
}
