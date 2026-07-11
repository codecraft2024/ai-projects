package com.instasimulator.api.healthcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceProviderNum {

    private String operatorNum;
    private String priority;
    private String keywordMsg;
    private String alias;
}
