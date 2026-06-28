package com.dnspex.repository;

import com.dnspex.entity.user.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {

    public Optional<Session> findByToken(String token) {
        return find("refreshToken", token).firstResultOptional();
    }

    public Optional<Session> findById(String id) {
        return find("id", id).firstResultOptional();
    }
}
