package com.instasimulator.stress;

import com.instasimulator.engine.EngineAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.instasimulator.stress")
@Import(EngineAutoConfiguration.class)
public class StressAutoConfiguration {
}
