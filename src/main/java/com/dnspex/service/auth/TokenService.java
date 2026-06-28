package com.dnspex.service.auth;

import com.dnspex.entity.user.Session;
import com.dnspex.entity.user.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String accessToken(User user, Session session) {
        Set<String> roles = user.getRoles().stream()
                .flatMap(role -> role.getInheritedRoles().stream())
                .collect(Collectors.toSet());

        return Jwt.issuer(this.issuer)
                .upn(user.getEmail())
                .claim("id", user.getId()) // uses to identify who taken actions at log assigned entities
                .claim("sid", session.getId())
                .subject(user.getId())
                .groups(roles)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 15))
                .sign();
    }
}
