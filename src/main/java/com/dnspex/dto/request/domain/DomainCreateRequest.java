package com.dnspex.dto.request.domain;

import jakarta.validation.constraints.NotBlank;

public record DomainCreateRequest (
        @NotBlank( message = "NAME_REQUIRED")
        String name
) {}
