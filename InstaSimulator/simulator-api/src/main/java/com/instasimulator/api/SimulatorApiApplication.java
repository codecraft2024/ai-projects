package com.instasimulator.api;

import com.instasimulator.engine.EngineAutoConfiguration;
import com.instasimulator.reports.ReportsAutoConfiguration;
import com.instasimulator.stress.StressAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRetry
@EnableScheduling
@Import({
        EngineAutoConfiguration.class,
        StressAutoConfiguration.class,
        ReportsAutoConfiguration.class
})
public class SimulatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApiApplication.class, args);
    }
}
