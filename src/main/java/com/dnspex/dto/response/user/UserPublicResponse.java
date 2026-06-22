package com.dnspex.dto.response.user;

import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.UserRole;

import java.time.LocalDateTime;
import java.util.Set;

public record UserPublicResponse (
        String id,
        String email,
        String displayName,
        Set<UserRole> roles,
        LocalDateTime createdAt
) implements UserResponse {
    public static UserPublicResponse of(User user) {
        return new UserPublicResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRoles(),
                user.getCreatedAt()
        );
    }
}
