package com.dnspex.dto.request.user.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest (
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID")
        String email,

        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, max = 48, message = "PASSWORD_LENGTH_INVALID")
        String password
) { }
