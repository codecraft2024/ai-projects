package com.tabeeby;

import com.tabeeby.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TabeebyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TabeebyApplication.class, args);
    }
}
