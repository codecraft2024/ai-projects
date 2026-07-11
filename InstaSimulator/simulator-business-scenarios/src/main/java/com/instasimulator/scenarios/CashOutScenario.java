package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.calls.account.GetBalanceCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.calls.payment.CashOutCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashOutScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public CashOutScenario(LoginCall loginCall,
                           GetAccountsCall getAccountsCall,
                           GetBalanceCall getBalanceCall,
                           CashOutCall cashOutCall,
                           LogoutCall logoutCall) {
        this.steps = List.of(loginCall, getAccountsCall, getBalanceCall, cashOutCall, logoutCall);
    }

    @Override
    public String name() {
        return "cash-out";
    }

    @Override
    public String description() {
        return "Login → accounts → balance → cash-out → logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
