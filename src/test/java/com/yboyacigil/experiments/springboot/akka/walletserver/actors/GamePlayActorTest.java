package com.yboyacigil.experiments.springboot.akka.walletserver.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Status;
import akka.testkit.javadsl.TestKit;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.ApplicationConfig;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.SpringExtension;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Balance;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Currency;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetBalance;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetCurrency;
import com.yboyacigil.experiments.springboot.akka.walletserver.services.AccountingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class GamePlayActorTest {

    private static final String GAME_PLAY_ACTOR_BEAN_NAME = "gamePlayActor";

    @Autowired
    private ActorSystem system;

    @Autowired
    private SpringExtension springExtension;

    @MockBean
    private AccountingService gisService;

    @Test
    public void should_ReceiveGetCurrencyReturnCurrency_When_PlayerInfoExists() {

        final Long pid = 1L;
        final String isoCurrencyCode = "SEK";

        AccountingService.PlayerInfo playerInfo = AccountingService.PlayerInfo.builder()
            .pid(pid)
            .isoCurrencyCode(isoCurrencyCode)
            .build();
        when(gisService.getPlayerInfo(pid)).thenReturn(Optional.of(playerInfo));

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetCurrency getCurrency = GetCurrency.builder()
                .pid(pid)
                .build();

            subject.tell(getCurrency, getRef());

            expectMsg(
                Duration.ofSeconds(3),
                Optional.of(Currency.builder()
                    .pid(pid)
                    .isoCurrencyCode(isoCurrencyCode)
                    .build()
                )
            );
        }};

        verify(gisService).getPlayerInfo(pid);
    }

    @Test
    public void should_ReceiveGetCurrencyReturnEmpty_When_NoPlayerInfoExists() {

        final Long pid = 1L;

        when(gisService.getPlayerInfo(pid)).thenReturn(Optional.empty());

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetCurrency getCurrency = GetCurrency.builder()
                .pid(pid)
                .build();

            subject.tell(getCurrency, getRef());

            expectMsg(
                Duration.ofSeconds(3),
                Optional.empty()
            );
        }};

        verify(gisService).getPlayerInfo(pid);
    }

    @Test
    public void should_ReceiveGetCurrencyReturnFailure_When_ExceptionThrown() {

        final Long pid = 1L;

        when(gisService.getPlayerInfo(pid)).thenThrow(new RuntimeException());

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetCurrency getCurrency = GetCurrency.builder()
                .pid(pid)
                .build();

            subject.tell(getCurrency, getRef());

            expectMsgClass(
                Duration.ofSeconds(3),
                Status.Failure.class
            );
        }};

        verify(gisService).getPlayerInfo(pid);
    }

    @Test
    public void should_ReceiveGetBalanceReturnBalance_When_PlayerInfoExists() {

        final Long pid = 1L;
        final String isoCurrencyCode = "SEK";
        final BigDecimal balance = BigDecimal.ONE;

        AccountingService.BalanceInfo balanceInfo = AccountingService.BalanceInfo.builder()
            .pid(pid)
            .isoCurrencyCode(isoCurrencyCode)
            .balance(balance)
            .build();
        when(gisService.getBalanceInfo(pid)).thenReturn(Optional.of(balanceInfo));

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetBalance getBalance = GetBalance.builder()
                .pid(pid)
                .gameCode("")
                .build();

            subject.tell(getBalance, getRef());

            expectMsg(
                Duration.ofSeconds(3),
                Optional.of(Balance.builder()
                    .pid(pid)
                    .isoCurrencyCode(isoCurrencyCode)
                    .balance(balance)
                    .build()
                )
            );
        }};

        verify(gisService).getBalanceInfo(pid);
    }

    @Test
    public void should_ReceiveGetBalanceReturnEmpty_When_NoPlayerInfoExists() {

        final Long pid = 1L;

        when(gisService.getBalanceInfo(pid)).thenReturn(Optional.empty());

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetBalance getBalance = GetBalance.builder()
                .pid(pid)
                .build();

            subject.tell(getBalance, getRef());

            expectMsg(
                Duration.ofSeconds(3),
                Optional.empty()
            );
        }};

        verify(gisService).getBalanceInfo(pid);
    }

    @Test
    public void should_ReceiveGetBalanceReturnFailure_When_ExceptionThrown() {

        final Long pid = 1L;

        when(gisService.getBalanceInfo(pid)).thenThrow(new RuntimeException());

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_ACTOR_BEAN_NAME));

            GetBalance getBalance = GetBalance.builder()
                .pid(pid)
                .build();

            subject.tell(getBalance, getRef());

            expectMsgClass(
                Duration.ofSeconds(3),
                Status.Failure.class
            );
        }};

        verify(gisService).getBalanceInfo(pid);
    }

}
