package com.instasimulator.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Lightweight account representation stored in simulation context.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

    private String accountId;
    private String accountType;
    private String currency;
    private BigDecimal balance;
    private boolean primary;
}
