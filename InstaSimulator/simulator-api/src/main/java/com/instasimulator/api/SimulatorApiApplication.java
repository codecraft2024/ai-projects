package com.instasimulator.api;

import com.instasimulator.scenarios.ScenariosAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ScenariosAutoConfiguration.class)
public class SimulatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApiApplication.class, args);
    }
}
