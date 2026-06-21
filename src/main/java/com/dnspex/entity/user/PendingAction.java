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
    public String token = TokenManager.generate();

    @Column(nullable = false)
    public LocalDateTime expiresAt;

    public LocalDateTime usedAt = null; //maybe boolean and scheduler to clear the pending action

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public PendingActionType type;

    @ManyToOne
    @JoinColumn
    public User user;

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
