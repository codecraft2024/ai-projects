package com.instasimulator.calls;

import com.instasimulator.config.ConfigAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.instasimulator.calls")
@Import(ConfigAutoConfiguration.class)
public class CallsAutoConfiguration {
}
