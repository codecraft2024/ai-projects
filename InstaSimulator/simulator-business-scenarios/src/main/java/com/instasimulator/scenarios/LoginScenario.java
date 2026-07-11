package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.calls.account.GetBalanceCall;
import com.instasimulator.calls.account.GetProfileCall;
import com.instasimulator.calls.account.GetTransactionsCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Typical daily mobile banking journey.
 */
@Component
public class LoginScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public LoginScenario(LoginCall loginCall,
                         GetProfileCall getProfileCall,
                         LogoutCall logoutCall) {
        this.steps = List.of(loginCall, getProfileCall, logoutCall);
    }

    @Override
    public String name() {
        return "login-scenario";
    }

    @Override
    public String description() {
        return "Login, fetch profile, logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
