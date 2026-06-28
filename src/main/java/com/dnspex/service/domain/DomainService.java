package com.dnspex.service.domain;

import com.dnspex.dto.request.domain.DomainCreateRequest;
import com.dnspex.entity.domain.Domain;
import com.dnspex.entity.user.User;
import com.dnspex.service.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;

@ApplicationScoped
public class DomainService {

    @Inject
    UserService userService;

    public Map<String, String> create(DomainCreateRequest request) {
        User user = this.userService.get();

        Domain domain = new Domain();
        domain.setName(request.name());
        domain.setUser(user);
        domain.persist();

        return Map.of(
                "id", domain.getId(),
                "name", domain.getName()
        );
    }
}
