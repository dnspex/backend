package com.dnspex.repository;

import com.dnspex.entity.user.PendingAction;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PendingActionRepository implements PanacheRepository<PendingAction> {

    public Optional<PendingAction> findByToken(String token) {
        return find("token", token).firstResultOptional();
    }
}
