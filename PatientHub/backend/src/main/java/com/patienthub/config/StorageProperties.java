package com.patienthub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "patienthub.storage")
public record StorageProperties(String uploadDir) {
}
