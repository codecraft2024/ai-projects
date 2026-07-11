package com.instasimulator.config;

import com.instasimulator.common.http.HttpClientConfig;
import com.instasimulator.config.properties.SimulatorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration module Spring setup.
 */
@Configuration
@ComponentScan("com.instasimulator.config")
@EnableConfigurationProperties(SimulatorProperties.class)
public class ConfigAutoConfiguration {

    @Bean
    public HttpClientConfig httpClientConfig(SimulatorProperties properties) {
        return HttpClientConfig.builder()
                .connectTimeoutMs(properties.getTimeouts().getConnectMs())
                .responseTimeoutMs(properties.getTimeouts().getReadMs())
                .maxRetries(properties.getRetry().getMaxAttempts())
                .retryBackoffMs(properties.getRetry().getBackoffMs())
                .build();
    }
}
