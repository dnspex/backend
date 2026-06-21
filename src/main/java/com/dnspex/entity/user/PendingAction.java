package com.dnspex.entity.user;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.util.enumeration.PendingActionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "user_pending_action")
public class PendingAction extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    public String token = UUID.randomUUID().toString(); //replace it with real random string

    public LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);

    public LocalDateTime usedAt = null;

    @Enumerated(EnumType.STRING)
    public PendingActionType type;

    @ManyToOne
    @JoinColumn
    public User user;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
