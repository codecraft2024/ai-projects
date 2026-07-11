package com.instasimulator.api.healthcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCheckResponse {

    private String code;
    private String result;
    private HealthCheckData data;

    @JsonProperty("checksum")
    private String checksum;

    @JsonProperty("checkSum")
    private String checkSumValue;
}
