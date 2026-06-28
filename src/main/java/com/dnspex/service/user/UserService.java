package com.dnspex.service.user;

import com.dnspex.entity.user.PendingAction;
import com.dnspex.entity.user.Session;
import com.dnspex.entity.user.User;
import com.dnspex.repository.UserRepository;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Context
    ContainerRequestContext context;

    @Transactional
    public void delete() {
        User user = this.get();

        List<Session> sessions = user.getSessions();
        sessions.forEach(Session::delete);
        sessions.clear();

        List<PendingAction> actions = user.getPendingActions();
        actions.forEach(PanacheEntityBase::delete);
        actions.clear();

        user.setDeactivatedAt(LocalDateTime.now());
        user.setState(UserState.DEACTIVATED);
        user.persist();
    }

    public User get() {
        String userId = context.getProperty("uid").toString();
        return this.findByIdAndActive(userId);
    }

    public User findByIdAndActive(String id) {
        return userRepository.findByIdAndActive(id)
                .orElseThrow(() -> new HttpResponse(
                     Response.Status.NOT_FOUND, "USER_NOT_FOUND"
                ));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new HttpResponse(
                     Response.Status.NOT_FOUND, "USER_NOT_FOUND"
                ));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
