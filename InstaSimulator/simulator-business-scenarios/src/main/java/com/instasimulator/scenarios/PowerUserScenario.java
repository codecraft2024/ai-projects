package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetAccountsCall;
import com.instasimulator.calls.account.GetBalanceCall;
import com.instasimulator.calls.account.GetProfileCall;
import com.instasimulator.calls.account.GetTransactionsCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.calls.auth.RefreshTokenCall;
import com.instasimulator.calls.payment.BillPaymentCall;
import com.instasimulator.calls.qr.QRGenerateCall;
import com.instasimulator.calls.qr.QRValidateCall;
import com.instasimulator.calls.transfer.TransferCall;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * High-activity power user journey covering multiple product areas.
 */
@Component
public class PowerUserScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public PowerUserScenario(LoginCall loginCall,
                             GetProfileCall getProfileCall,
                             GetAccountsCall getAccountsCall,
                             GetBalanceCall getBalanceCall,
                             TransferCall transferCall,
                             BillPaymentCall billPaymentCall,
                             QRGenerateCall qrGenerateCall,
                             QRValidateCall qrValidateCall,
                             GetTransactionsCall getTransactionsCall,
                             RefreshTokenCall refreshTokenCall,
                             LogoutCall logoutCall) {
        this.steps = List.of(
                loginCall,
                getProfileCall,
                getAccountsCall,
                getBalanceCall,
                transferCall,
                billPaymentCall,
                qrGenerateCall,
                qrValidateCall,
                getTransactionsCall,
                refreshTokenCall,
                logoutCall
        );
    }

    @Override
    public String name() {
        return "power-user";
    }

    @Override
    public String description() {
        return "Extended multi-feature power user journey";
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
