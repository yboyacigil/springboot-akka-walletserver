package com.yboyacigil.experiments.springboot.akka.walletserver.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance implements GameMessage {
    private Long pid;
    private String isoCurrencyCode;
    private BigDecimal balance;
}
