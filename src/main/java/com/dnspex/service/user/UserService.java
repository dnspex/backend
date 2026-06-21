package com.dnspex.service.user;

import com.dnspex.entity.user.User;
import com.dnspex.repository.UserRepository;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    JsonWebToken jsonWebToken;

    public User get() {
        var userId = jsonWebToken.getSubject();
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
