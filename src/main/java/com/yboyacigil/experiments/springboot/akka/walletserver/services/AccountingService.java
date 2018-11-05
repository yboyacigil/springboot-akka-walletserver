package com.yboyacigil.experiments.springboot.akka.walletserver.services;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public interface AccountingService {

    Optional<PlayerInfo> getPlayerInfo(Long pid);

    Optional<BalanceInfo> getBalanceInfo(Long pid);

    Optional<WithdrawResult> withdraw(Withdraw withdraw);

    Optional<DepositResult> deposit(Deposit deposit);

    Optional<RollbackResult> rollback(Rollback rollback);

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

    @Data
    @Builder
    class Withdraw {
        private Long pid;
        private BigDecimal amount;
        private String game;
        private String gameRoundRef;
        private String transactionRef;
        private Date transactionDate;
    }

    @Data
    @Builder
    class WithdrawResult {
        private BigDecimal balance;
        private String serverTransactionRef;
        private Integer responseCode;
        private String responseMessage;
    }

    @Data
    @Builder
    class Deposit {
        private Long pid;
        private BigDecimal amount;
        private String game;
        private String gameRoundRef;
        private String transactionRef;
        private Date startDate;
        private Date transactionDate;
    }

    @Data
    @Builder
    class DepositResult {
        private BigDecimal balance;
        private String serverTransactionRef;
        private Integer responseCode;
        private String responseMessage;
    }

    @Data
    @Builder
    class Rollback {
        private Long pid;
        private String game;
        private String gameRoundRef;
        private String transactionRef;
    }

    @Data
    @Builder
    class RollbackResult {
        private BigDecimal balance;
        private String serverTransactionRef;
        private Integer responseCode;
        private String responseMessage;
    }

}
