package com.instasimulator.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartStressRequest {

    @NotBlank
    private String scenarioName;

    @Min(1)
    private int virtualUsers = 10;

    @Min(1)
    private long durationSeconds = 60;

    @Min(0)
    private long rampUpSeconds = 10;

    @Min(0)
    private long rampDownSeconds = 5;

    @Min(0)
    private long thinkTimeMinMs = 200;

    @Min(0)
    private long thinkTimeMaxMs = 1500;
}
