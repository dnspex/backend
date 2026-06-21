package com.dnspex.util.enumeration;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum PendingActionType {
    VERIFY_EMAIL(LocalDateTime.now().plusDays(1)),
    PASSWORD_RESET(LocalDateTime.now().plusHours(1));

    private final LocalDateTime expiresAt;

    PendingActionType(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
