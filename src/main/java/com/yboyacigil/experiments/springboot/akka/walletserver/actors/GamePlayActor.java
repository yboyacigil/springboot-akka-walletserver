package com.yboyacigil.experiments.springboot.akka.walletserver.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Status;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Balance;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Currency;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetBalance;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetCurrency;
import com.yboyacigil.experiments.springboot.akka.walletserver.services.AccountingService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class GamePlayActor extends AbstractLoggingActor {

    private AccountingService accountingService;

    public GamePlayActor(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(GetCurrency.class, this::receiveGetCurrency)
            .match(GetBalance.class, this::receiveGetBalance)
            .matchAny(o -> log().info("Received unknown message: {}", o))
            .build();
    }

    private void receiveGetCurrency(GetCurrency getCurrency) {
        try {
            Optional<AccountingService.PlayerInfo> maybePlayerInfo = accountingService.getPlayerInfo(getCurrency.getPid());

            Optional<Currency> maybeCurrency = maybePlayerInfo.map(playerInfo ->
                Currency.builder()
                    .pid(playerInfo.getPid())
                    .isoCurrencyCode(playerInfo.getIsoCurrencyCode())
                    .build()
            );

            getSender().tell(maybeCurrency, getSelf());
        } catch(Exception e) {
            log().error("Error in getting player info from GIS service", e);
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    private void receiveGetBalance(GetBalance getBalance) {
        try {
            Optional<AccountingService.BalanceInfo> maybeBalanceInfo = accountingService.getBalanceInfo(getBalance.getPid());

            Optional<Balance> maybeBalance = maybeBalanceInfo.map(balanceInfo ->
                Balance.builder()
                    .pid(balanceInfo.getPid())
                    .isoCurrencyCode(balanceInfo.getIsoCurrencyCode())
                    .balance(balanceInfo.getBalance())
                    .build()
            );

            getSender().tell(maybeBalance, getSelf());
        } catch(Exception e) {
            log().info("Error in getting balance info from GIS service", e);
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }
}
