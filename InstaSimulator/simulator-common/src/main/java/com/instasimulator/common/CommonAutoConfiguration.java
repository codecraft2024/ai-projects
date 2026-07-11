package com.instasimulator.common;

import com.instasimulator.common.http.HttpClientConfig;
import com.instasimulator.common.http.SimulatorHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring beans shared across simulator modules.
 */
@Configuration
public class CommonAutoConfiguration {

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    public SimulatorHttpClient simulatorHttpClient(HttpClientConfig httpClientConfig) {
        return new SimulatorHttpClient(httpClientConfig);
    }
}
