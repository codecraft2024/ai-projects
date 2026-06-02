package com.patienthub.service;

import com.patienthub.model.HealthStatus;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthStatus getStatus() {
        return new HealthStatus("UP", "PatientHub API is running");
    }
}
