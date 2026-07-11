package com.instasimulator.stress.stats;

import com.instasimulator.common.enums.StressJobStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Live statistics for an in-flight stress job.
 */
@Data
public class StressStatistics {

    private final String jobId;
    private final String scenarioName;
    private volatile StressJobStatus status = StressJobStatus.IDLE;
    private volatile int targetUsers;
    private volatile int activeUsers;
    private Instant startedAt;
    private Instant finishedAt;

    private final LongAdder totalIterations = new LongAdder();
    private final LongAdder successCount = new LongAdder();
    private final LongAdder failureCount = new LongAdder();
    private final LongAdder totalDurationMs = new LongAdder();
    private final AtomicLong minDurationMs = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxDurationMs = new AtomicLong(0);
    private final List<Long> samples = Collections.synchronizedList(new ArrayList<>());

    public StressStatistics(String jobId, String scenarioName) {
        this.jobId = jobId;
        this.scenarioName = scenarioName;
    }

    public void recordSuccess(long durationMs) {
        totalIterations.increment();
        successCount.increment();
        recordDuration(durationMs);
    }

    public void recordFailure(long durationMs) {
        totalIterations.increment();
        failureCount.increment();
        recordDuration(durationMs);
    }

    private void recordDuration(long durationMs) {
        totalDurationMs.add(durationMs);
        minDurationMs.accumulateAndGet(durationMs, Math::min);
        maxDurationMs.accumulateAndGet(durationMs, Math::max);
        if (samples.size() < 10_000) {
            samples.add(durationMs);
        }
    }

    public Snapshot snapshot() {
        long total = totalIterations.sum();
        long avg = total == 0 ? 0 : totalDurationMs.sum() / total;
        return Snapshot.builder()
                .jobId(jobId)
                .scenarioName(scenarioName)
                .status(status)
                .targetUsers(targetUsers)
                .activeUsers(activeUsers)
                .totalIterations(total)
                .successCount(successCount.sum())
                .failureCount(failureCount.sum())
                .averageDurationMs(avg)
                .minDurationMs(minDurationMs.get() == Long.MAX_VALUE ? 0 : minDurationMs.get())
                .maxDurationMs(maxDurationMs.get())
                .p50(percentile(50))
                .p90(percentile(90))
                .p95(percentile(95))
                .p99(percentile(99))
                .startedAt(startedAt)
                .finishedAt(finishedAt)
                .build();
    }

    private long percentile(int pct) {
        List<Long> copy;
        synchronized (samples) {
            if (samples.isEmpty()) {
                return 0;
            }
            copy = new ArrayList<>(samples);
        }
        Collections.sort(copy);
        int index = Math.min(copy.size() - 1, (int) Math.ceil(pct / 100.0 * copy.size()) - 1);
        return copy.get(Math.max(0, index));
    }

    @Data
    @Builder
    public static class Snapshot {
        private String jobId;
        private String scenarioName;
        private StressJobStatus status;
        private int targetUsers;
        private int activeUsers;
        private long totalIterations;
        private long successCount;
        private long failureCount;
        private long averageDurationMs;
        private long minDurationMs;
        private long maxDurationMs;
        private long p50;
        private long p90;
        private long p95;
        private long p99;
        private Instant startedAt;
        private Instant finishedAt;
    }
}
