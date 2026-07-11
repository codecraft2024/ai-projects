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
 * End-of-day browse flow: profile, accounts, balance, transactions.
 */
@Component
public class DailyUserScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public DailyUserScenario(LoginCall loginCall,
                             GetProfileCall getProfileCall,
                             GetAccountsCall getAccountsCall,
                             GetBalanceCall getBalanceCall,
                             GetTransactionsCall getTransactionsCall,
                             LogoutCall logoutCall) {
        this.steps = List.of(
                loginCall,
                getProfileCall,
                getAccountsCall,
                getBalanceCall,
                getTransactionsCall,
                logoutCall
        );
    }

    @Override
    public String name() {
        return "daily-user";
    }

    @Override
    public String description() {
        return "Login → profile → accounts → balance → transactions → logout";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
