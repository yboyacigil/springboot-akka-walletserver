package com.yboyacigil.experiments.springboot.akka.walletserver.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

public interface GISService {

    Optional<PlayerInfo> getPlayerInfo(Long pid);

    Optional<BalanceInfo> getBalanceInfo(Long pid);

    @Data
    @Builder
    class PlayerInfo {
        private Long pid;
        private String isoCurrencyCode;
    }

    @Data
    @Builder
    class BalanceInfo {
        private Long pid;
        private String isoCurrencyCode;
        private BigDecimal balance;
    }
}
