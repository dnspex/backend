package com.dnspex.dto.response.user;

import com.dnspex.entity.user.User;

import java.time.LocalDateTime;

public record UserPublicResponse (
        String id,
        String email,
        LocalDateTime createdAt
) implements UserResponse {
    public static UserPublicResponse of(User o) {
        return new UserPublicResponse(
                o.getId(),
                o.getEmail(),
                o.getCreatedAt()
        );
    }
}
