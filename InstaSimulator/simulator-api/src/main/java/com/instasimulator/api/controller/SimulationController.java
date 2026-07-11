package com.instasimulator.api.controller;

import com.instasimulator.api.dto.RunScenarioRequest;
import com.instasimulator.api.dto.StartStressRequest;
import com.instasimulator.api.service.SimulationService;
import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.reports.model.ExecutionReport;
import com.instasimulator.stress.stats.StressStatistics;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/scenario/run")
    public ScenarioResult runScenario(@Valid @RequestBody RunScenarioRequest request) {
        return simulationService.runScenario(request);
    }

    @PostMapping("/stress/start")
    public StressStatistics.Snapshot startStress(@Valid @RequestBody StartStressRequest request) {
        return simulationService.startStress(request);
    }

    @PostMapping("/stress/stop/{jobId}")
    public ResponseEntity<StressStatistics.Snapshot> stopStress(@PathVariable String jobId) {
        return simulationService.stopStress(jobId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return simulationService.status();
    }

    @GetMapping("/metrics")
    public Map<String, Object> metrics() {
        return simulationService.metrics();
    }

    @GetMapping("/reports")
    public ExecutionReport reports() throws IOException {
        return simulationService.generateReport();
    }
}
