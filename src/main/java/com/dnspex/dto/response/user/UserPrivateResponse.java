package com.dnspex.dto.response.user;

import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

public record UserPrivateResponse (
        String id,
        String email,
        Set<UserRole> roles,
        LocalDateTime createdAt
) implements UserResponse {
    public static UserPrivateResponse of(User o) {
        return new UserPrivateResponse(
                o.getId(),
                o.getEmail(),
                o.getRoles(),
                o.getCreatedAt()
        );
    }
}
