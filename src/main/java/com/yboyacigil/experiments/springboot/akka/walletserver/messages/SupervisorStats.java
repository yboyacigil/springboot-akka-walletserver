package com.yboyacigil.experiments.springboot.akka.walletserver.messages;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SupervisorStats {
    private Integer size;
    private Set<Long> pids;
}
