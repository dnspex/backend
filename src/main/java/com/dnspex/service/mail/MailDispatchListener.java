package com.dnspex.service.mail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MailDispatchListener {

    @Inject
    MailService mailService;

    void onMail(@Observes(during = TransactionPhase.AFTER_SUCCESS) MailJob job) {
        Thread.ofVirtual().start(() -> {
            try {
                this.mailService.deliver(job);
            } catch (RuntimeException e) {
                log.warn("Unexpected error sending email to {} with template {}", job.to(), job.templateId(), e);
            }
        });
    }
}
