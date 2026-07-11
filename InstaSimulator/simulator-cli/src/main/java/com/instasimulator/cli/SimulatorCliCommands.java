package com.instasimulator.cli;

import com.instasimulator.common.dto.ScenarioResult;
import com.instasimulator.config.properties.SimulatorProperties;
import com.instasimulator.engine.executor.ScenarioExecutor;
import com.instasimulator.reports.ReportGenerator;
import com.instasimulator.stress.runner.StressConfig;
import com.instasimulator.stress.runner.StressRunner;
import com.instasimulator.stress.stats.StressStatistics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Picocli-based CLI entrypoint for scenario and stress execution.
 */
@Component
@Command(name = "simulator", mixinStandardHelpOptions = true,
        description = "Mobile App Simulation Framework CLI",
        subcommands = {
                SimulatorCliCommands.RunScenario.class,
                SimulatorCliCommands.ListScenarios.class,
                SimulatorCliCommands.RunStress.class
        })
public class SimulatorCliCommands implements CommandLineRunner, Callable<Integer> {

    private final ScenarioExecutor scenarioExecutor;
    private final StressRunner stressRunner;
    private final ReportGenerator reportGenerator;
    private final SimulatorProperties properties;

    public SimulatorCliCommands(ScenarioExecutor scenarioExecutor,
                                StressRunner stressRunner,
                                ReportGenerator reportGenerator,
                                SimulatorProperties properties) {
        this.scenarioExecutor = scenarioExecutor;
        this.stressRunner = stressRunner;
        this.reportGenerator = reportGenerator;
        this.properties = properties;
    }

    @Override
    public void run(String... args) {
        int exit = new CommandLine(this).execute(args);
        if (exit != 0) {
            System.exit(exit);
        }
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        return 0;
    }

    @Command(name = "run", description = "Run a business scenario")
    static class RunScenario implements Callable<Integer> {

        @CommandLine.ParentCommand
        private SimulatorCliCommands parent;

        @Option(names = {"-s", "--scenario"}, required = true, description = "Scenario name")
        private String scenario;

        @Override
        public Integer call() throws Exception {
            ScenarioResult result = parent.scenarioExecutor.run(scenario);
            var report = parent.reportGenerator.build(List.of(result), null);
            Path dir = Path.of(parent.properties.getReportsDir());
            parent.reportGenerator.writeHtml(report, dir);
            parent.reportGenerator.writeJson(report, dir);
            System.out.printf("Scenario=%s status=%s durationMs=%d%n",
                    result.getScenarioName(), result.getStatus(), result.getDurationMs());
            return result.isSuccess() ? 0 : 1;
        }
    }

    @Command(name = "list", description = "List available scenarios")
    static class ListScenarios implements Callable<Integer> {

        @CommandLine.ParentCommand
        private SimulatorCliCommands parent;

        @Override
        public Integer call() {
            parent.scenarioExecutor.availableScenarios().forEach(System.out::println);
            return 0;
        }
    }

    @Command(name = "stress", description = "Start a stress test (blocks until complete)")
    static class RunStress implements Callable<Integer> {

        @CommandLine.ParentCommand
        private SimulatorCliCommands parent;

        @Option(names = {"-s", "--scenario"}, required = true)
        private String scenario;

        @Option(names = {"-u", "--users"}, defaultValue = "10")
        private int users;

        @Option(names = {"-d", "--duration"}, defaultValue = "30")
        private long durationSeconds;

        @Override
        public Integer call() throws Exception {
            StressStatistics.Snapshot snap = parent.stressRunner.start(StressConfig.builder()
                    .scenarioName(scenario)
                    .virtualUsers(users)
                    .durationSeconds(durationSeconds)
                    .rampUpSeconds(Math.min(5, durationSeconds))
                    .build());

            String jobId = snap.getJobId();
            while (true) {
                Thread.sleep(1000);
                var status = parent.stressRunner.status(jobId).orElse(null);
                if (status == null) {
                    return 1;
                }
                System.out.printf("job=%s status=%s iterations=%d success=%d failed=%d avgMs=%d%n",
                        status.getJobId(), status.getStatus(), status.getTotalIterations(),
                        status.getSuccessCount(), status.getFailureCount(), status.getAverageDurationMs());
                switch (status.getStatus()) {
                    case COMPLETED, STOPPED, FAILED -> {
                        return status.getFailureCount() > 0 ? 1 : 0;
                    }
                    default -> {
                    }
                }
            }
        }
    }
}
