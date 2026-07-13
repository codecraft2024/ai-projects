package com.instasimulator.api;

import com.instasimulator.calls.CallsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CallsAutoConfiguration.class)
public class SimulatorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApiApplication.class, args);
    }
}
