package com.v2com.service;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class MigrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger("MigrationService"); 
    
    @Inject
    Flyway flyway;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Initializing DB with Flyway...");
        flyway.clean();
        flyway.migrate();

        MigrationInfo current = flyway.info().current();

        if(current != null) {
            LOGGER.info(current.getVersion().getVersion());
            LOGGER.info(current.getState().toString());
        } else {
            LOGGER.info("Flyway version not specified!");
        }
    }
}