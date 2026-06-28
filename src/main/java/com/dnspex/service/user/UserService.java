package com.dnspex.service.user;

import com.dnspex.entity.user.Session;
import com.dnspex.entity.user.User;
import com.dnspex.repository.UserRepository;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    public void delete() {
        User user = this.get();

        List<Session> sessions = user.getSessions();
        sessions.forEach(Session::delete);

        user.setDeactivatedAt(LocalDateTime.now());
        user.setState(UserState.DEACTIVATED);

        user.persist();
    }

    public User get() {
        String userId = context.getProperty("uid").toString();
        return this.findByIdAndActive(userId);
    }

    public User findById(String id) {
        return userRepository.findByIdOptional(Long.valueOf(id))
                .orElseThrow(() -> new HttpResponse(
                        Response.Status.NOT_FOUND, "USER_NOT_FOUND"
                ));
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
