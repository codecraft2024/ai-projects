package com.instasimulator.config;

import com.instasimulator.config.http.HttpClientConfig;
import com.instasimulator.config.jackson.JacksonConfig;
import com.instasimulator.config.properties.SimulatorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(SimulatorProperties.class)
@Import({JacksonConfig.class, HttpClientConfig.class})
public class ConfigAutoConfiguration {
}
