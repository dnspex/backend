package com.dnspex.repository;

import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.UserState;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<User> findByEmailAndActive(String email) {
        return find("email = ?1 and state = ?2", email, UserState.ACTIVE).firstResultOptional();
    }

    public Optional<User> findByIdAndActive(String id) {
        return find("id = ?1 and state = ?2", id, UserState.ACTIVE).firstResultOptional();
    }

    public List<User> findAllByState(UserState state) {
        return list("state", state);
    }

    public List<User> findAllDeactivatedBefore(LocalDateTime cutoff) {
        return list("state = ?1 and deactivatedAt < ?2", UserState.DEACTIVATED, cutoff);
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
