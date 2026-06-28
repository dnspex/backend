package com.dnspex.task;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DomainValidationTask {

    @Scheduled(cron = "{dnspex.domain.validation.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void validate() {
        //check all domains of the txt record and when entry doesnt exist, then set state to DomainState.NOT_FOUND
        //when a entry found but the token is mismatch then set state to DomainState.MISMATCH
    }
}
