package com.yboyacigil.experiments.springboot.akka.walletserver.endpoints;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import com.yboyacigil.experiments.springboot.akka.walletserver.config.SpringExtension;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.Currency;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GameMessage;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetBalance;
import com.yboyacigil.experiments.springboot.akka.walletserver.messages.GetCurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Slf4j
@Service
@Path("/walletserver")
public class WalletServerEndpoint {

    @Autowired
    private ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    private ActorRef gamePlaySupervisor;

    @PostConstruct
    public void init() {
        gamePlaySupervisor = actorSystem.actorOf(springExtension.props("gamePlaySupervisor"));
    }

    @GET
    @Path("/players/{pid}/account/currency")
    @Produces(MediaType.APPLICATION_JSON)
    public void getCurrency(@Suspended final AsyncResponse asyncResponse, @PathParam("pid") Long pid) {

        GetCurrency getCurrency = GetCurrency.builder()
            .pid(pid)
            .build();

        askForResult(asyncResponse, getCurrency);
    }

    @GET
    @Path("/players/{pid}/account/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public void getBalance(@Suspended final AsyncResponse asyncResponse, @PathParam("pid") Long pid) {

        GetBalance getBalance = GetBalance.builder()
            .pid(pid)
            .gameCode("not-important-atm")
            .build();

        askForResult(asyncResponse, getBalance);
    }

    @SuppressWarnings("unchecked")
    private void askForResult(final AsyncResponse asyncResponse, GameMessage gameMessage) {
        Future<Object> result = Patterns.ask(gamePlaySupervisor, gameMessage, 1000L);

        FutureConverters.toJava(result).toCompletableFuture()
            .thenApply(o -> {
                if (o instanceof Optional<?>) {
                    Optional<Currency> optionalCurrency = (Optional<Currency>) o;
                    if (optionalCurrency.isPresent()) {
                        log.debug("Returning currency result: {}", o);
                        return asyncResponse.resume(Response.ok().entity(((Optional<Currency>) o).get()).build());
                    } else {
                        throw new NotFoundException("No player/currency found");
                    }
                } else {
                    log.warn("Unexpected result: {}", o);
                    throw new InternalServerErrorException("Unexpected result");
                }
            })
            .exceptionally(asyncResponse::resume);
    }

}
