package com.instasimulator.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RunScenarioRequest {

    @NotBlank
    private String scenarioName;

    private String username;
    private String password;
}
