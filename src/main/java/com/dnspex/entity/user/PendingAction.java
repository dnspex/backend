package com.dnspex.entity.user;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.util.enumeration.PendingActionType;
import com.dnspex.util.math.IdentifierManager;
import com.dnspex.util.math.TokenManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "user_pending_action")
@EntityListeners(AuditingEntityListener.class)
public class PendingAction extends AbstractAuditingEntity {

    @Id
    public String id = IdentifierManager.generate("pa");

    @Column(nullable = false, unique = true)
    public String token = TokenManager.generate(); //replace it with real random string

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
