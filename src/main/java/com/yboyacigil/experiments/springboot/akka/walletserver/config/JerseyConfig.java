package com.yboyacigil.experiments.springboot.akka.walletserver.config;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig  extends ResourceConfig {

    public JerseyConfig() {
        packages("com.yboyacigil.experiments.springboot.akka.walletserver.endpoints");


        register(LoggingFeature.class);
        property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");
    }
}
