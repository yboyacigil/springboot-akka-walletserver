package com.yboyacigil.experiments.springboot.akka.walletserver.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
@ComponentScan(basePackages = "com.yboyacigil.experiments.springboot.akka.walletserver")
public class ApplicationConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Bean(destroyMethod = "terminate")
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("gie-actor-system", akkaConfig());
        springExtension.initialize(applicationContext);
        return system;
    }

    @Bean
    public Config akkaConfig() {
        return ConfigFactory.load();
    }

}
