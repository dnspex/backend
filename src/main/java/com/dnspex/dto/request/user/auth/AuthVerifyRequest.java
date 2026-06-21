package com.dnspex.dto.request.user.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthVerifyRequest(
        @NotBlank(message = "TOKEN_REQUIRED")
        String token
) {
}
