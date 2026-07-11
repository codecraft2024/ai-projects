package com.instasimulator.api.healthcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCheckData {

    private String keywordMsg;
    private List<ServiceProviderNum> serviceProverderNum = new ArrayList<>();
    private List<ServiceProviderNum> localProviders = new ArrayList<>();
    private Integer waitTime;
    private List<ServiceProviderNum> roamingProviders = new ArrayList<>();
    private String minVersion;
    private String maxVersion;
    private String appPubKey;
    private String loginAllow;
    private String registerAllow;
    private String customerCareNumber;
    private String customerCareWatsappNumber;
    private String iosMinVersion;
    private String iosMaxVersion;
    private String listCategoryVersion;
    private String listServiceVersion;
    private String listServiceVersionAr;
    private String billInqConfig;
    private String atmCashoutFee;
    private String forgotIpaEnabled;
    private Object custom;
}
