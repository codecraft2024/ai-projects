package com.tabeeby.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tabeeby.storage")
public record StorageProperties(String uploadDir) {
}
