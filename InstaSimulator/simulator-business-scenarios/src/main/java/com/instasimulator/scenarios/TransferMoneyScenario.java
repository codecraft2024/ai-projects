package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.calls.account.GetBalanceCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.calls.transfer.TransferCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Money transfer journey.
 */
@Component
public class TransferMoneyScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public TransferMoneyScenario(LoginCall loginCall,
                                 GetAccountsCall getAccountsCall,
                                 GetBalanceCall getBalanceCall,
                                 TransferCall transferCall,
                                 LogoutCall logoutCall) {
        this.steps = List.of(
                loginCall,
                getAccountsCall,
                getBalanceCall,
                transferCall,
                getBalanceCall,
                logoutCall
        );
    }

    @Override
    public String name() {
        return "transfer-money";
    }

    @Override
    public String description() {
        return "Login → accounts → balance → transfer → balance → logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
