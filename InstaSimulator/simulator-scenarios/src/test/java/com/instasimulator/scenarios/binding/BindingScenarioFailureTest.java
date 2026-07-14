package com.instasimulator.scenarios.binding;

import com.instasimulator.calls.bind1.Bind1Service;
import com.instasimulator.calls.healthcheck.HealthCheckService;
import com.instasimulator.calls.register1.Register1Service;
import com.instasimulator.common.dto.CallExecution;
import com.instasimulator.common.dto.bind1.Bind1Request;
import com.instasimulator.common.dto.bind1.Bind1Response;
import com.instasimulator.common.dto.healthcheck.HealthCheckRequest;
import com.instasimulator.common.dto.healthcheck.HealthCheckResponse;
import com.instasimulator.common.dto.register1.Register1Request;
import com.instasimulator.common.dto.register1.Register1Response;
import com.instasimulator.common.dto.scenario.ScenarioResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BindingScenarioFailureTest {

    @Mock
    private HealthCheckService healthCheckService;
    @Mock
    private Bind1Service bind1Service;
    @Mock
    private Register1Service register1Service;

    @InjectMocks
    private BindingScenario bindingScenario;

    @Test
    void failsWhenLastStepReturnsNonSuccessCode() {
        when(healthCheckService.runSuccess()).thenReturn(health(true, "ok"));
        when(bind1Service.runSuccess()).thenReturn(bind1("00000", "ok"));
        when(register1Service.runSuccess()).thenReturn(register1("99994", "Internal error: missing salt"));

        ScenarioResult result = bindingScenario.run();

        assertFalse(result.success());
        assertEquals("FAILURE", result.status());
        assertEquals(3, result.steps().size());
        assertFalse(result.steps().get(2).success());
    }

    private static CallExecution<HealthCheckRequest, HealthCheckResponse> health(boolean ok, String message) {
        Instant now = Instant.now();
        return new CallExecution<>(null, new HealthCheckResponse(ok, message), now, now, 0, List.of());
    }

    private static CallExecution<Bind1Request, Bind1Response> bind1(String code, String result) {
        Instant now = Instant.now();
        return new CallExecution<>(null, new Bind1Response(code, result, null, null, null), now, now, 0, List.of());
    }

    private static CallExecution<Register1Request, Register1Response> register1(String code, String result) {
        Instant now = Instant.now();
        return new CallExecution<>(null, new Register1Response(code, result, null, null, null), now, now, 0, List.of());
    }
}
