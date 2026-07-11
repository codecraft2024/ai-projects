package com.instasimulator.scenarios;

import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.calls.payment.BillPaymentCall;
import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillPaymentScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public BillPaymentScenario(LoginCall loginCall,
                               GetAccountsCall getAccountsCall,
                               BillPaymentCall billPaymentCall,
                               LogoutCall logoutCall) {
        this.steps = List.of(loginCall, getAccountsCall, billPaymentCall, logoutCall);
    }

    @Override
    public String name() {
        return "bill-payment";
    }

    @Override
    public String description() {
        return "Login → accounts → bill payment → logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
