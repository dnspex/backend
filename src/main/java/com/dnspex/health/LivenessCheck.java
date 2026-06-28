package com.dnspex.health;

import com.dnspex.service.mail.MailService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class LivenessCheck implements HealthCheck {

    @Inject
    MailService mailService;

    @Override
    public HealthCheckResponse call() {
        return this.mail();
    }

    private HealthCheckResponse mail() {
        return this.mailService.isReachable()
                ? HealthCheckResponse.up("Mail-Service healthy")
                : HealthCheckResponse.down("Mail-Service is not healthy");
    }
}