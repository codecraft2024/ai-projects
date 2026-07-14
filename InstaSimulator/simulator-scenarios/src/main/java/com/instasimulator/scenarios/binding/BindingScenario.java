package com.instasimulator.scenarios.binding;

import com.instasimulator.calls.bind1.Bind1Service;
import com.instasimulator.calls.healthcheck.HealthCheckService;
import com.instasimulator.calls.register1.Register1Service;
import com.instasimulator.common.dto.scenario.ScenarioResult;
import com.instasimulator.common.dto.scenario.ScenarioStepResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BindingScenario {

    public static final String NAME = "binding-scenario";

    private static final Logger log = LoggerFactory.getLogger(BindingScenario.class);

    private final HealthCheckService healthCheckService;
    private final Bind1Service bind1Service;
    private final Register1Service register1Service;

    public BindingScenario(HealthCheckService healthCheckService,
                           Bind1Service bind1Service,
                           Register1Service register1Service) {
        this.healthCheckService = healthCheckService;
        this.bind1Service = bind1Service;
        this.register1Service = register1Service;
    }

    public ScenarioResult run() {
        Instant startedAt = Instant.now();
        log.info("[scenario:binding] Started");

        List<ScenarioStepResult> steps = new ArrayList<>();

        steps.add(runHealthCheck());
        if (!steps.getLast().success()) {
            return failed(startedAt, steps);
        }

        steps.add(runBind1());
        if (!steps.getLast().success()) {
            return failed(startedAt, steps);
        }

        steps.add(runRegister1());
        if (!steps.getLast().success()) {
            return failed(startedAt, steps);
        }

        return passed(startedAt, steps);
    }

    private ScenarioResult failed(Instant startedAt, List<ScenarioStepResult> steps) {
        ScenarioStepResult failedStep = steps.getLast();
        ScenarioResult result = done(startedAt, false, steps);
        log.error("[scenario:binding] SCENARIO FAILED | step={} | code={} | status={}",
                failedStep.stepName(), failedStep.statusCode(), result.status());
        return result;
    }

    private ScenarioResult passed(Instant startedAt, List<ScenarioStepResult> steps) {
        ScenarioResult result = done(startedAt, true, steps);
        log.info("[scenario:binding] SCENARIO PASSED | steps={}", steps.size());
        return result;
    }

    private static ScenarioResult done(Instant startedAt, boolean success, List<ScenarioStepResult> steps) {
        Instant finishedAt = Instant.now();
        long durationMs = Math.max(0, finishedAt.toEpochMilli() - startedAt.toEpochMilli());
        return ScenarioResult.of(NAME, success, startedAt, finishedAt, durationMs, steps);
    }

    private ScenarioStepResult runHealthCheck() {
        var response = healthCheckService.runSuccess().response();
        return step("health-check", response.success(), response.success() ? "OK" : "FAILED", response.message());
    }

    private ScenarioStepResult runBind1() {
        var response = bind1Service.runSuccess().response();
        return step("bind1", response.isSuccess(), response.code(), response.result());
    }

    private ScenarioStepResult runRegister1() {
        var response = register1Service.runSuccess().response();
        return step("register1", response.isSuccess(), response.code(), response.result());
    }

    private static ScenarioStepResult step(String name, boolean success, String code, String message) {
        log.info("[scenario:binding] {} → {} | code={}", name, success ? "SUCCESS" : "FAILURE", code);
        return ScenarioStepResult.of(name, success, code, message);
    }
}
