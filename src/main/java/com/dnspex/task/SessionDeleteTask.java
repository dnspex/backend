package com.dnspex.task;

import com.dnspex.entity.user.Session;
import com.dnspex.service.user.SessionService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.scheduler.Scheduled;
import io.sentry.Sentry;
import io.sentry.SentryAttribute;
import io.sentry.SentryAttributes;
import io.sentry.SentryLogLevel;
import io.sentry.logger.SentryLogParameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SessionDeleteTask {

    @Inject
    SessionService sessionService;

    @Transactional
    @Scheduled(cron = "{dnspex.scheduled.session.delete.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void delete() {
        List<Session> sessions = this.sessionService.findAllExpired();

        if (sessions.isEmpty()) return;

        sessions.forEach(PanacheEntityBase::delete);

        SentryLogParameters params = SentryLogParameters.create(
                SentryAttributes.of(
                        SentryAttribute.integerAttribute("count", sessions.size()),
                        SentryAttribute.stringAttribute("deletedAt", LocalDateTime.now().toString())
                ));

        params.setOrigin("session.delete.task");
        Sentry.logger().log(SentryLogLevel.INFO, params, "Deleted sessions that expired 7 days ago");
    }
}
