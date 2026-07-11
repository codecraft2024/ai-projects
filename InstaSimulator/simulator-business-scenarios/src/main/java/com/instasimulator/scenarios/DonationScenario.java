package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.calls.payment.DonateCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DonationScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public DonationScenario(LoginCall loginCall,
                            GetAccountsCall getAccountsCall,
                            DonateCall donateCall,
                            LogoutCall logoutCall) {
        this.steps = List.of(loginCall, getAccountsCall, donateCall, logoutCall);
    }

    @Override
    public String name() {
        return "donation";
    }

    @Override
    public String description() {
        return "Login → accounts → donate → logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
