package com.dnspex.util.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum UserRole {
    USER(1, 1000),
    MODERATOR(4, 2000),
    ADMIN(6, 4000);

    private final int id;
    private final int power;

    UserRole(int id, int power) {
        this.id = id;
        this.power = power;
    }

    public Set<String> getInheritedRoles() {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.power <= this.power)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
