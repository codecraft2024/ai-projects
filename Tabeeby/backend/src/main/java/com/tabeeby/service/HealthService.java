package com.tabeeby.service;

import com.tabeeby.model.HealthStatus;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthStatus getStatus() {
        return new HealthStatus("UP", "Tabeeby API is running");
    }
}
