package com.dnspex.entity.user;

import com.dnspex.entity.auth.Session;
import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.util.enumeration.Roles;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.json.JSONBuilder;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.UserDefinition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@UserDefinition
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    public String email;

    @Password
    @Column(nullable = false)
    public String password;

    public LocalDateTime lastLoginAt = null;

    public LocalDateTime activatedAt = null;

    @Enumerated(EnumType.STRING)
    public UserState state = UserState.INACTIVE;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Roles> roles = Set.of(Roles.USER);

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Session> sessions = new ArrayList<>();

    public Map<String, Object> toScheme() {
        return JSONBuilder.create()
                .add("id", id)
                .add("email", email)
                .add("createdAt", createdAt)
                .add("lastModifiedAt", lastModifiedAt)
                .add("roles", roles)
                .add("lastLoginAt", lastLoginAt)
                .toMap();
    }

    public boolean isActivated() {
        return activatedAt != null;
    }

    public void activate() {
        this.activatedAt = LocalDateTime.now();
    }
}
