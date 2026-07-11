package com.instasimulator.stress.runner;

import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.common.enums.StressJobStatus;
import com.instasimulator.common.util.IdGenerator;
import com.instasimulator.common.util.RandomDataGenerator;
import com.instasimulator.engine.executor.ScenarioExecutor;
import com.instasimulator.stress.stats.StressStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Concurrent stress engine with ramp-up, think time, looping, and graceful stop.
 */
@Component
public class StressRunner {

    private static final Logger log = LoggerFactory.getLogger(StressRunner.class);

    private final ScenarioExecutor scenarioExecutor;
    private final Map<String, StressStatistics> jobs = new ConcurrentHashMap<>();
    private final Map<String, AtomicBoolean> stopFlags = new ConcurrentHashMap<>();
    private final Map<String, ExecutorService> executors = new ConcurrentHashMap<>();

    public StressRunner(ScenarioExecutor scenarioExecutor) {
        this.scenarioExecutor = scenarioExecutor;
    }

    public StressStatistics.Snapshot start(StressConfig config) {
        String jobId = IdGenerator.executionId();
        StressStatistics stats = new StressStatistics(jobId, config.getScenarioName());
        stats.setTargetUsers(config.getVirtualUsers());
        stats.setStatus(StressJobStatus.RAMPING_UP);
        stats.setStartedAt(Instant.now());
        jobs.put(jobId, stats);

        AtomicBoolean stop = new AtomicBoolean(false);
        stopFlags.put(jobId, stop);

        ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
        executors.put(jobId, pool);

        pool.submit(() -> runJob(jobId, config, stats, stop, pool));
        log.info("Stress job started jobId={} scenario={} users={}",
                jobId, config.getScenarioName(), config.getVirtualUsers());
        return stats.snapshot();
    }

    public Optional<StressStatistics.Snapshot> stop(String jobId) {
        AtomicBoolean stop = stopFlags.get(jobId);
        StressStatistics stats = jobs.get(jobId);
        if (stop == null || stats == null) {
            return Optional.empty();
        }
        stats.setStatus(StressJobStatus.STOPPING);
        stop.set(true);
        ExecutorService pool = executors.get(jobId);
        if (pool != null) {
            pool.shutdownNow();
        }
        stats.setStatus(StressJobStatus.STOPPED);
        stats.setFinishedAt(Instant.now());
        return Optional.of(stats.snapshot());
    }

    public Optional<StressStatistics.Snapshot> status(String jobId) {
        return Optional.ofNullable(jobs.get(jobId)).map(StressStatistics::snapshot);
    }

    public List<StressStatistics.Snapshot> allJobs() {
        return jobs.values().stream().map(StressStatistics::snapshot).toList();
    }

    private void runJob(String jobId, StressConfig config, StressStatistics stats,
                        AtomicBoolean stop, ExecutorService pool) {
        try {
            long endAt = System.currentTimeMillis() + config.getDurationSeconds() * 1000L;
            long rampMs = Math.max(1, config.getRampUpSeconds() * 1000L);
            long delayBetweenUsers = rampMs / Math.max(1, config.getVirtualUsers());

            List<Future<?>> workers = new ArrayList<>();
            for (int i = 0; i < config.getVirtualUsers() && !stop.get(); i++) {
                final int userIndex = i;
                workers.add(pool.submit(() -> virtualUserLoop(
                        userIndex, config, stats, stop, endAt)));
                stats.setActiveUsers(i + 1);
                Thread.sleep(delayBetweenUsers);
            }

            stats.setStatus(StressJobStatus.RUNNING);

            for (Future<?> worker : workers) {
                try {
                    worker.get();
                } catch (Exception ignored) {
                    // individual failures already recorded
                }
            }

            if (!stop.get()) {
                stats.setStatus(StressJobStatus.RAMPING_DOWN);
                Thread.sleep(Math.max(0, config.getRampDownSeconds() * 1000L));
                stats.setStatus(StressJobStatus.COMPLETED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            stats.setStatus(StressJobStatus.STOPPED);
        } catch (Exception e) {
            log.error("Stress job {} failed: {}", jobId, e.getMessage());
            stats.setStatus(StressJobStatus.FAILED);
        } finally {
            stats.setActiveUsers(0);
            stats.setFinishedAt(Instant.now());
            pool.shutdown();
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void virtualUserLoop(int userIndex, StressConfig config, StressStatistics stats,
                                 AtomicBoolean stop, long endAt) {
        int loops = 0;
        while (!stop.get() && System.currentTimeMillis() < endAt) {
            if (config.getLoopsPerUser() > 0 && loops >= config.getLoopsPerUser()) {
                break;
            }
            long think = RandomDataGenerator.thinkTimeMs(config.getThinkTimeMinMs(), config.getThinkTimeMaxMs());
            try {
                Thread.sleep(think);
                ScenarioResult result = scenarioExecutor.run(config.getScenarioName());
                if (result.isSuccess()) {
                    stats.recordSuccess(result.getDurationMs());
                } else {
                    stats.recordFailure(result.getDurationMs());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                stats.recordFailure(0);
                log.debug("Virtual user {} iteration failed: {}", userIndex, e.getMessage());
            }
            loops++;
        }
    }
}
