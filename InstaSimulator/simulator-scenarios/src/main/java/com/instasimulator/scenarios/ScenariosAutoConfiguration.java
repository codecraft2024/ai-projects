package com.instasimulator.scenarios;

import com.instasimulator.calls.CallsAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.instasimulator.scenarios")
@Import(CallsAutoConfiguration.class)
public class ScenariosAutoConfiguration {
}
