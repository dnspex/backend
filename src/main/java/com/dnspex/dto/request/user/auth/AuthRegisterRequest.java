package com.dnspex.dto.request.user.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest (
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID")
        String email,

        @NotBlank(message = "PASSWORD_REQUIRED")
        @Min(value = 8, message = "PASSWORD_TOO_SHORT")
        @Max(value = 48, message = "PASSWORD_TOO_LONG")

        String password
) { }
