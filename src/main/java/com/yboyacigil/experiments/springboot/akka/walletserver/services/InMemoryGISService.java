package com.yboyacigil.experiments.springboot.akka.walletserver.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
public class InMemoryGISService implements GISService {

    private Random random = new Random();

    public Optional<PlayerInfo> getPlayerInfo(Long pid) {
        if (pid == 0) {
            throw new RuntimeException();
        }
        if (pid == 42) {
            return Optional.empty();
        }
        return
            Optional.of(
                PlayerInfo.builder()
                    .pid(pid)
                    .isoCurrencyCode("SEK")
                    .build()
            );
    }

    public Optional<BalanceInfo> getBalanceInfo(Long pid) {
        return
            Optional.of(
                BalanceInfo.builder()
                    .pid(pid)
                    .isoCurrencyCode("SEK")
                    .balance(BigDecimal.valueOf(random.nextDouble()))
                    .build()
            );
    }

}
