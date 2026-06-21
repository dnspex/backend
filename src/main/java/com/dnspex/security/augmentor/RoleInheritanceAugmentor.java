package com.dnspex.security.augmentor;

import com.dnspex.util.enumeration.UserRole;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class RoleInheritanceAugmentor implements SecurityIdentityAugmentor {

    /**
     * This method is called to augment the SecurityIdentity with inherited roles.
     * It takes the existing roles and adds any inherited roles defined in the UserRole enum.
     *
     * @param securityIdentity The original SecurityIdentity
     * @param authenticationRequestContext The authentication request context
     * @return A new SecurityIdentity with augmented roles
     */
    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity securityIdentity, AuthenticationRequestContext authenticationRequestContext) {
        return Uni.createFrom().item(() -> {
            Set<String> augmentedRoles = securityIdentity.getRoles().stream()
                    .flatMap(roleName -> {
                        try {
                            UserRole role = UserRole.valueOf(roleName);
                            return role.getInheritedRoles().stream();
                        } catch (IllegalArgumentException e) {
                            return Stream.of(roleName);
                        }
                    })
                    .collect(Collectors.toSet());

            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(securityIdentity);
            augmentedRoles.forEach(builder::addRole);

            return builder.build();
        });
    }
}
