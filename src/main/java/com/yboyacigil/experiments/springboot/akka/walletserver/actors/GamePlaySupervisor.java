package com.yboyacigil.experiments.springboot.akka.walletserver.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.SpringExtension;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GameMessage;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetSupervisorStats;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.SupervisorStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class GamePlaySupervisor extends AbstractLoggingActor {

    @Autowired
    private SpringExtension springExtension;

    private Map<Long, ActorRef> gamePlayActorMap;

    @Override
    public void preStart() throws Exception {
        log().info("Starting up");
        gamePlayActorMap = new HashMap<>();
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(GameMessage.class, message -> {
                ActorRef playerAccountActor = gamePlayActorMap.computeIfAbsent(message.getPid(), pid -> {
                    ActorRef actor = getContext().actorOf(
                        springExtension.props("gamePlayActor"), String.valueOf(pid));
                    getContext().watch(actor);
                    log().info("A new game play actor for pid: {} created", message.getPid());
                    return actor;
                });
                playerAccountActor.tell(message, getSender());
            })
            .match(GetSupervisorStats.class, message -> {
                SupervisorStats stats = SupervisorStats.builder()
                    .size(gamePlayActorMap.size())
                    .pids(gamePlayActorMap.keySet())
                    .build();
                log().info("Returning stats: {}", stats);
                getSender().tell(stats, getSender());
            })
            .match(Terminated.class, message -> {
                Long pid = Long.parseLong(message.actor().path().name());

                gamePlayActorMap.remove(pid);

                ActorRef actor = getContext().actorOf(
                    springExtension.props("gamePlayActor"), String.valueOf(pid));
                getContext().watch(actor);
                gamePlayActorMap.put(pid, actor);

                log().info("Game play actor with pid: {} terminated. A new one created", pid);
            })
            .matchAny(o -> log().info("Received unknown message: {}", o))
            .build();
    }

    @Override
    public void postStop() throws Exception {
        log().info("Shutting down");
        gamePlayActorMap.clear();
        super.postStop();
    }
}
