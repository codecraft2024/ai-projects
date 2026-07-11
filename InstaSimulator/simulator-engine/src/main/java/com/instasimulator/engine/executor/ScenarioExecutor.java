package com.instasimulator.engine.executor;

import com.instasimulator.common.context.SimulationContext;
import com.instasimulator.common.dto.CallResult;
import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.common.enums.ExecutionStatus;
import com.instasimulator.common.exception.ScenarioExecutionException;
import com.instasimulator.common.util.IdGenerator;
import com.instasimulator.common.util.RandomDataGenerator;
import com.instasimulator.core.call.ApiCall;
import com.instasimulator.core.metrics.MetricsCollector;
import com.instasimulator.core.scenario.BusinessScenario;
import com.instasimulator.engine.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Executes business scenarios step-by-step with context, metrics, and failure handling.
 */
@Component
public class ScenarioExecutor {

    private static final Logger log = LoggerFactory.getLogger(ScenarioExecutor.class);

    private final Map<String, BusinessScenario> scenarios;
    private final SessionManager sessionManager;
    private final MetricsCollector metricsCollector;
    private final Map<String, ScenarioResult> recentResults = new ConcurrentHashMap<>();

    public ScenarioExecutor(List<BusinessScenario> scenarioList,
                            SessionManager sessionManager,
                            MetricsCollector metricsCollector) {
        this.scenarios = scenarioList.stream()
                .collect(Collectors.toMap(BusinessScenario::name, Function.identity(), (a, b) -> a));
        this.sessionManager = sessionManager;
        this.metricsCollector = metricsCollector;
    }

    public ScenarioResult run(String scenarioName) {
        return run(scenarioName, sessionManager.createContext());
    }

    public ScenarioResult run(String scenarioName, String username, String password) {
        return run(scenarioName, sessionManager.createContext(username, password));
    }

    public ScenarioResult run(String scenarioName, SimulationContext context) {
        BusinessScenario scenario = Optional.ofNullable(scenarios.get(scenarioName))
                .orElseThrow(() -> new ScenarioExecutionException(scenarioName, "Unknown scenario"));

        Instant startedAt = Instant.now();
        String executionId = IdGenerator.executionId();
        metricsCollector.scenarioStarted();

        ScenarioResult result = ScenarioResult.builder()
                .executionId(executionId)
                .scenarioName(scenario.name())
                .correlationId(context.getCorrelationId())
                .userId(context.getUserId())
                .status(ExecutionStatus.RUNNING)
                .startedAt(startedAt)
                .build();

        log.info("Starting scenario={} executionId={} correlationId={} userId={}",
                scenario.name(), executionId, context.getCorrelationId(), context.getUserId());

        try {
            scenario.beforeScenario(context);

            for (ApiCall call : scenario.steps()) {
                long think = RandomDataGenerator.thinkTimeMs(50, 300);
                Thread.sleep(think);

                CallResult callResult = call.execute(context);
                result.addCallResult(callResult);
                metricsCollector.recordCall(call.name(), callResult.getDurationMs());

                if (!callResult.isSuccess()) {
                    throw new ScenarioExecutionException(scenario.name(),
                            "Call failed: " + call.name() + " - " + callResult.getErrorMessage());
                }
            }

            result.setStatus(ExecutionStatus.SUCCESS);
            scenario.afterScenario(context, result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.setStatus(ExecutionStatus.CANCELLED);
            result.setErrorMessage("Interrupted");
        } catch (Exception e) {
            result.setStatus(ExecutionStatus.FAILED);
            result.setErrorMessage(e.getMessage());
            log.error("Scenario {} failed: {}", scenario.name(), e.getMessage());
        } finally {
            result.setFinishedAt(Instant.now());
            result.setDurationMs(result.getFinishedAt().toEpochMilli() - startedAt.toEpochMilli());
            metricsCollector.scenarioFinished(result.isSuccess(), result.getDurationMs());
            recentResults.put(executionId, result);
            sessionManager.remove(context.getSessionId());
            log.info("Finished scenario={} executionId={} status={} durationMs={}",
                    scenario.name(), executionId, result.getStatus(), result.getDurationMs());
        }

        return result;
    }

    public Optional<ScenarioResult> getResult(String executionId) {
        return Optional.ofNullable(recentResults.get(executionId));
    }

    public List<String> availableScenarios() {
        return scenarios.keySet().stream().sorted().toList();
    }

    public Map<String, ScenarioResult> recentResults() {
        return Map.copyOf(recentResults);
    }
}
