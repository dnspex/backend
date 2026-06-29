package com.dnspex.task;

import com.dnspex.entity.user.User;
import com.dnspex.repository.UserRepository;
import io.quarkus.scheduler.Scheduled;
import io.sentry.Sentry;
import io.sentry.SentryAttribute;
import io.sentry.SentryAttributes;
import io.sentry.SentryLogLevel;
import io.sentry.logger.SentryLogParameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class UserDeleteTask {

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "dnspex.scheduled.user.delete.after.days")
    Integer userDeleteAfter;

    @Transactional
    @Scheduled(cron = "{dnspex.scheduled.user.delete.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void delete() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(userDeleteAfter);

        List<User> users = this.userRepository.findAllDeactivatedBefore(cutoff);
        if (users.isEmpty()) return;

        users.forEach(User::delete);

        SentryLogParameters params = SentryLogParameters.create(
                SentryAttributes.of(
                        SentryAttribute.integerAttribute("count", users.size()),
                        SentryAttribute.stringAttribute("deletedAt", LocalDateTime.now().toString())
                ));

        params.setOrigin("user.delete.task");
        Sentry.logger().log(SentryLogLevel.INFO, params, "Deleted users whose accounts have been deactivated for " + userDeleteAfter + " days");
    }
}
