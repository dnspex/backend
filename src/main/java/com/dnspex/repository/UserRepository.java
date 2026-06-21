package com.dnspex.repository;

import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.UserState;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

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

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
