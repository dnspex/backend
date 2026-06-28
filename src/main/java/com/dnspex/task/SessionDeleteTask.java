package com.dnspex.task;

import com.dnspex.entity.user.Session;
import com.dnspex.service.user.SessionService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SessionDeleteTask {

    @Inject
    SessionService sessionService;

    @Scheduled(cron = "{dnspex.session.delete.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void delete() {
        List<Session> sessions = this.sessionService.findAllExpired();
        sessions.forEach(PanacheEntityBase::delete);
    }
}
