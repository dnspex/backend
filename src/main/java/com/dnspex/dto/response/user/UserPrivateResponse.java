package com.dnspex.dto.response.user;

import com.dnspex.dto.response.session.SessionResponse;
import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.UserRole;
import com.dnspex.util.enumeration.UserState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record UserPrivateResponse (
        String id,
        String email,
        String displayName,
        Set<UserRole> roles,
        UserState state,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        List<SessionResponse> sessions
) implements UserResponse {
    public static UserPrivateResponse of(User user) {
        return new UserPrivateResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRoles(),
                user.getState(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getSessions().stream().map(SessionResponse::of).toList()
        );
    }
}