package com.dnspex.dto.response.session;

import com.dnspex.entity.user.Session;

import java.time.LocalDateTime;

public record SessionResponse (
        String id,
        String userEmail,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
    public static SessionResponse of(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getUser().getEmail(),
                session.getCreatedAt(),
                session.getExpiresAt()
        );
    }
}
