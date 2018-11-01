package com.yboyacigil.experiments.springboot.akka.walletserver.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBalance implements GameMessage {
    private Long pid;
    private String gameCode;
}
