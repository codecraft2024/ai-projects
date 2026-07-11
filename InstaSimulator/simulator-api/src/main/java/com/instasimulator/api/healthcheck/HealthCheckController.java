package com.instasimulator.api.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/instapay/health-check")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @PostMapping("/success")
    public HealthCheckApiResponse success() {
        return healthCheckService.runSuccess();
    }

    @PostMapping("/failure")
    public HealthCheckApiResponse failure() {
        return healthCheckService.runFailure();
    }

    @PostMapping("/failure/{scenarioId}")
    public HealthCheckApiResponse failureByScenario(@PathVariable String scenarioId) {
        return healthCheckService.runFailure(HealthCheckFailureScenario.fromId(scenarioId));
    }

    @GetMapping("/failure/scenarios")
    public Map<String, Object> listFailureScenarios() {
        List<Map<String, String>> scenarios = Arrays.stream(HealthCheckFailureScenario.values())
                .map(s -> Map.of(
                        "id", s.getId(),
                        "expectedCode", s.getExpectedCode(),
                        "expectedResult", s.getExpectedResult()))
                .toList();
        return Map.of("scenarios", scenarios, "count", scenarios.size());
    }
}
