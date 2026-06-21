package com.dnspex.dto.request.user.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLogoutRequest (
        @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
        String refreshToken
) {
}
