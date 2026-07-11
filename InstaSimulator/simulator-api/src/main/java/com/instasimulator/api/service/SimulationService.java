package com.instasimulator.api.service;

import com.instasimulator.api.dto.RunScenarioRequest;
import com.instasimulator.api.dto.StartStressRequest;
import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.core.metrics.MetricsCollector;
import com.instasimulator.engine.executor.ScenarioExecutor;
import com.instasimulator.reports.ReportGenerator;
import com.instasimulator.reports.model.ExecutionReport;
import com.instasimulator.stress.runner.StressConfig;
import com.instasimulator.stress.runner.StressRunner;
import com.instasimulator.stress.stats.StressStatistics;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SimulationService {

    private final ScenarioExecutor scenarioExecutor;
    private final StressRunner stressRunner;
    private final MetricsCollector metricsCollector;
    private final ReportGenerator reportGenerator;
    private final SimulatorProperties properties;
    private final List<ScenarioResult> history = new ArrayList<>();

    public SimulationService(ScenarioExecutor scenarioExecutor,
                             StressRunner stressRunner,
                             MetricsCollector metricsCollector,
                             ReportGenerator reportGenerator,
                             SimulatorProperties properties) {
        this.scenarioExecutor = scenarioExecutor;
        this.stressRunner = stressRunner;
        this.metricsCollector = metricsCollector;
        this.reportGenerator = reportGenerator;
        this.properties = properties;
    }

    public ScenarioResult runScenario(RunScenarioRequest request) {
        ScenarioResult result;
        if (request.getUsername() != null && request.getPassword() != null) {
            result = scenarioExecutor.run(request.getScenarioName(),
                    request.getUsername(), request.getPassword());
        } else {
            result = scenarioExecutor.run(request.getScenarioName());
        }
        synchronized (history) {
            history.add(result);
        }
        return result;
    }

    public StressStatistics.Snapshot startStress(StartStressRequest request) {
        StressConfig config = StressConfig.builder()
                .scenarioName(request.getScenarioName())
                .virtualUsers(request.getVirtualUsers())
                .durationSeconds(request.getDurationSeconds())
                .rampUpSeconds(request.getRampUpSeconds())
                .rampDownSeconds(request.getRampDownSeconds())
                .thinkTimeMinMs(request.getThinkTimeMinMs())
                .thinkTimeMaxMs(request.getThinkTimeMaxMs())
                .build();
        return stressRunner.start(config);
    }

    public Optional<StressStatistics.Snapshot> stopStress(String jobId) {
        return stressRunner.stop(jobId);
    }

    public Map<String, Object> status() {
        return Map.of(
                "scenarios", scenarioExecutor.availableScenarios(),
                "metrics", metricsCollector.snapshot(),
                "stressJobs", stressRunner.allJobs()
        );
    }

    public Map<String, Object> metrics() {
        return metricsCollector.snapshot();
    }

    public ExecutionReport generateReport() throws IOException {
        List<ScenarioResult> snapshot;
        synchronized (history) {
            snapshot = new ArrayList<>(history);
        }
        ExecutionReport report = reportGenerator.build(snapshot, null);
        Path dir = Path.of(properties.getReportsDir());
        reportGenerator.writeJson(report, dir);
        reportGenerator.writeCsv(report, dir);
        reportGenerator.writeHtml(report, dir);
        return report;
    }

    public List<ScenarioResult> history() {
        synchronized (history) {
            return List.copyOf(history);
        }
    }
}
