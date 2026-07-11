package com.instasimulator.scenarios;

import com.instasimulator.calls.account.GetProfileCall;
import com.instasimulator.calls.auth.LoginCall;
import com.instasimulator.calls.auth.LogoutCall;
import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.util.RandomDataGenerator;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.scenario.BusinessScenario;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Simulates a newly registered user logging in for the first time.
 */
@Component
public class RegistrationScenario implements BusinessScenario {

    private final List<ApiCall> steps;

    public RegistrationScenario(LoginCall loginCall,
                                GetProfileCall getProfileCall,
                                LogoutCall logoutCall) {
        this.steps = List.of(loginCall, getProfileCall, logoutCall);
    }

    @Override
    public String name() {
        return "registration";
    }

    @Override
    public String description() {
        return "First-time login after registration";
    }

    @Override
    public void beforeScenario(SimulationContext context) {
        BusinessScenario.super.beforeScenario(context);
        context.put("registrationEmail", RandomDataGenerator.email());
        context.put("registrationPhone", RandomDataGenerator.phone());
    }

    @Override
    public List<ApiCall> steps() {
        return steps;
    }
}
