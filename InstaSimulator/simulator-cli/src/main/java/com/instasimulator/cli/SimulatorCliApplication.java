package com.instasimulator.cli;

import com.instasimulator.engine.EngineAutoConfiguration;
import com.instasimulator.reports.ReportsAutoConfiguration;
import com.instasimulator.stress.StressAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;

@SpringBootApplication
@Import({
        EngineAutoConfiguration.class,
        StressAutoConfiguration.class,
        ReportsAutoConfiguration.class
})
public class SimulatorCliApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SimulatorCliApplication.class, args)));
    }
}
