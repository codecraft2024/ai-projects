package com.twinzy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.twinzy.config.TwinzyProperties;

@SpringBootApplication
@EnableConfigurationProperties(TwinzyProperties.class)
public class TwinzyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwinzyApplication.class, args);
    }
}
