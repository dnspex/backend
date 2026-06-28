package com.dnspex.task;

import com.dnspex.service.user.UserService;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountDeleteTask {

    @Inject
    UserService userService;

    @Scheduled(cron = "{dnspex.account.delete.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void delete() {

    }
}
