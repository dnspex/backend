package com.dnspex.service.user;

import com.dnspex.dto.response.user.UserPrivateResponse;
import com.dnspex.dto.response.user.UserPublicResponse;
import com.dnspex.dto.response.user.UserResponse;
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

    public UserResponse get(String id) {
        User sessionOwner = this.getSafe();

        User user = this.findByIdAndActive(id);
        if (!user.getId().equals(sessionOwner.getId())) return UserPublicResponse.of(user);

        return UserPrivateResponse.of(user);
    }


    public User getSafe() {
        var userId = jsonWebToken.getSubject();
        return this.findById(userId);
    }

    public User get() {
        var userId = jsonWebToken.getSubject();
        return this.findById(userId);
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
