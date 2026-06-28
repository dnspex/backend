package com.dnspex.entity.user;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.domain.Domain;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.entity.monitor.Monitor;
import com.dnspex.util.enumeration.UserRole;
import com.dnspex.util.enumeration.UserState;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public LocalDateTime deactivatedAt = null;

    @Enumerated(EnumType.STRING)
    public UserState state = UserState.INACTIVE;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> roles = Set.of(UserRole.USER);

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PendingAction> pendingActions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Monitor> monitors = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Domain> domains = new ArrayList<>();
}
