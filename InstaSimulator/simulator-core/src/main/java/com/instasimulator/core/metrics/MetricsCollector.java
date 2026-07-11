package com.instasimulator.core.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Lightweight in-memory metrics collected during simulation runs.
 */
public class MetricsCollector {

    private final LongAdder totalScenarios = new LongAdder();
    private final LongAdder successfulScenarios = new LongAdder();
    private final LongAdder failedScenarios = new LongAdder();
    private final LongAdder totalCalls = new LongAdder();
    private final LongAdder totalDurationMs = new LongAdder();
    private final Map<String, LongAdder> callCounts = new ConcurrentHashMap<>();
    private final Map<String, LongAdder> callDurations = new ConcurrentHashMap<>();
    private final AtomicLong activeExecutions = new AtomicLong();

    public void scenarioStarted() {
        activeExecutions.incrementAndGet();
        totalScenarios.increment();
    }

    public void scenarioFinished(boolean success, long durationMs) {
        activeExecutions.decrementAndGet();
        if (success) {
            successfulScenarios.increment();
        } else {
            failedScenarios.increment();
        }
        totalDurationMs.add(durationMs);
    }

    public void recordCall(String callName, long durationMs) {
        totalCalls.increment();
        callCounts.computeIfAbsent(callName, k -> new LongAdder()).increment();
        callDurations.computeIfAbsent(callName, k -> new LongAdder()).add(durationMs);
    }

    public Map<String, Object> snapshot() {
        Map<String, Object> snapshot = new ConcurrentHashMap<>();
        snapshot.put("totalScenarios", totalScenarios.sum());
        snapshot.put("successfulScenarios", successfulScenarios.sum());
        snapshot.put("failedScenarios", failedScenarios.sum());
        snapshot.put("totalCalls", totalCalls.sum());
        snapshot.put("activeExecutions", activeExecutions.get());
        snapshot.put("averageScenarioDurationMs",
                totalScenarios.sum() == 0 ? 0 : totalDurationMs.sum() / totalScenarios.sum());
        snapshot.put("callCounts", toMap(callCounts));
        snapshot.put("callDurationsMs", toMap(callDurations));
        return snapshot;
    }

    public void reset() {
        totalScenarios.reset();
        successfulScenarios.reset();
        failedScenarios.reset();
        totalCalls.reset();
        totalDurationMs.reset();
        callCounts.clear();
        callDurations.clear();
        activeExecutions.set(0);
    }

    private static Map<String, Long> toMap(Map<String, LongAdder> source) {
        Map<String, Long> result = new ConcurrentHashMap<>();
        source.forEach((k, v) -> result.put(k, v.sum()));
        return result;
    }
}
