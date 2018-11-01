package com.yboyacigil.experiments.springboot.akka.walletserver.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.ApplicationConfig;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.SpringExtension;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetCurrency;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetSupervisorStats;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.SupervisorStats;
import com.yboyacigil.experiments.springboot.akka.walletserver.services.GISService;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Duration;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)

public class GamePlaySupervisorTest {

    private static final String GAME_PLAY_SUPERVISOR_BEAN_NAME = "gamePlaySupervisor";

    @Autowired
    private ActorSystem system;

    @Autowired
    private SpringExtension springExtension;

    @MockBean
    private GISService gisService;


    @Test
    public void should_ReceiveGetSupervisorStatsReturnEmpty_When_NoGamePlayActorCreated() {

        new TestKit(system) {{
            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_SUPERVISOR_BEAN_NAME));

            GetSupervisorStats getSupervisorStats = GetSupervisorStats.builder().build();

            subject.tell(getSupervisorStats, getRef());

            SupervisorStats stats = SupervisorStats.builder()
                .size(0).pids(Sets.newHashSet()).build();

            expectMsg(Duration.ofSeconds(3), stats);
        }};

    }

    @Test
    public void should_ReceiveGetSupervisorStatsReturnValues_After_GamePlayActorsCreated() {
        Long pid1 = 1L;
        Long pid2 = 2L;

        new TestKit(system) {{

            final ActorRef subject = system.actorOf(springExtension.props(GAME_PLAY_SUPERVISOR_BEAN_NAME));

            GetCurrency getCurrency1 = GetCurrency.builder()
                .pid(pid1)
                .build();
            subject.tell(getCurrency1, getRef());

            GetCurrency getCurrency2 = GetCurrency.builder()
                .pid(pid2)
                .build();
            subject.tell(getCurrency2, getRef());

            GetSupervisorStats getSupervisorStats = GetSupervisorStats.builder().build();

            subject.tell(getSupervisorStats, getRef());

            SupervisorStats stats = SupervisorStats.builder()
                .size(2).pids(Sets.newHashSet(Arrays.asList(pid1, pid2))).build();

            expectMsg(Duration.ofSeconds(3), stats);
        }};

    }

}
