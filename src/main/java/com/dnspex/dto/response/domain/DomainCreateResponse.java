package com.dnspex.dto.response.domain;

import com.dnspex.entity.domain.Domain;

public record DomainCreateResponse (
        String id,
        String name,
        String token
) {
    public static DomainCreateResponse of(Domain domain) {
        return new DomainCreateResponse(
                domain.getId(),
                domain.getName(),
                domain.getToken()
        );
    }
}
