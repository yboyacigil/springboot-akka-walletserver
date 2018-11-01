package com.yboyacigil.experiments.springboot.akka.walletserver.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency implements GameMessage {
    private Long pid;
    private String isoCurrencyCode;
}
