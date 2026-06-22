package com.dnspex.entity.user;

import com.dnspex.entity.auth.Session;
import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.util.enumeration.UserRole;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.json.JSONBuilder;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractAuditingEntity {

    @Id
    public String id = IdentifierManager.generate("user");

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    public String displayName;

    @Column(nullable = false)
    public String password;

    public LocalDateTime lastLoginAt = null;

    public LocalDateTime activatedAt = null;

    @Enumerated(EnumType.STRING)
    public UserState state = UserState.INACTIVE;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> roles = Set.of(UserRole.USER);

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Session> sessions = new ArrayList<>();

    public void activate() {
        this.activatedAt = LocalDateTime.now();
    }
}
