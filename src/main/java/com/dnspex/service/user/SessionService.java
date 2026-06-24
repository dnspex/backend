package com.dnspex.service.user;

import com.dnspex.entity.auth.Session;
import com.dnspex.entity.user.User;
import com.dnspex.repository.SessionRepository;
import com.dnspex.service.auth.TokenService;
import com.dnspex.util.math.TokenManager;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.Map;

@ApplicationScoped
public class SessionService {

    @Inject
    TokenService tokenService;

    @Inject
    SessionRepository sessionRepository;

    @Transactional
    public Map<String, String> refresh(String refreshToken) {
        Session session = this.findByRefreshToken(refreshToken);

        if(session.isExpired()) {
            session.delete();
            throw new HttpResponse(Response.Status.BAD_REQUEST, "REFRESH_TOKEN_EXPIRED");
        }

        Session refreshedSession = this.refresh(session);

        return this.create(refreshedSession);
    }

    public Session findById(String sessionId) {
        return this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new HttpResponse(
                        Response.Status.BAD_REQUEST, "INVALID_SESSION"
                ));
    }

    public Session findByRefreshToken(String refreshToken) {
        return this.sessionRepository.findByToken(refreshToken)
                .orElseThrow(() -> new HttpResponse(
                        Response.Status.BAD_REQUEST, "INVALID_REFRESH_TOKEN"
                ));
    }

    public Map<String, String> create(Session session) {
        return Map.of("accessToken", tokenService.accessToken(session.getUser(), session), "refreshToken", session.getRefreshToken());
    }

    @Transactional
    public Session refresh(Session session) {
        session.setRefreshToken(TokenManager.generate());
        session.setLastUsedAt(LocalDateTime.now());
        session.persist();

        return session;
    }

    public Map<String, String> create(User user, String ipAddress, String deviceHint) {
        Session session = new Session();
        session.setUser(user);
        session.setIpAddress(ipAddress);
        session.setDeviceHint(deviceHint);
        session.persist();

        return this.create(session);
    }
}
