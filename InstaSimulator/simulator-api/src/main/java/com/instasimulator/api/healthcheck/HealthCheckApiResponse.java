package com.instasimulator.api.healthcheck;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckApiResponse {

    private String scenario;
    private int httpStatus;
    private long durationMs;
    private String correlationId;
    private HealthCheckResponse body;
}
