package com.dnspex.repository;

import com.dnspex.entity.auth.Session;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {

    public Optional<Session> findByToken(String token) {
        return find("refreshToken", token).firstResultOptional();
    }
}
