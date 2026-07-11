package com.instasimulator.core;

import com.instasimulator.core.metrics.MetricsCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Core module Spring configuration.
 */
@Configuration
@ComponentScan("com.instasimulator.core")
public class CoreAutoConfiguration {

    @Bean
    public MetricsCollector metricsCollector() {
        return new MetricsCollector();
    }
}
