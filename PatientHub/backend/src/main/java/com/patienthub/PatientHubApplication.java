package com.patienthub;

import com.patienthub.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class PatientHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientHubApplication.class, args);
    }
}
