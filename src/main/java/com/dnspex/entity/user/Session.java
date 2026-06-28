package com.dnspex.entity.user;

import com.dnspex.util.math.IdentifierManager;
import com.dnspex.util.math.TokenManager;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "session")
@Entity
public class Session extends PanacheEntityBase {

    @Id
    public String id = IdentifierManager.generate("session");

    @Column(unique = true)
    public String refreshToken = TokenManager.generate(); //replace it with real token like jwt

    @CreationTimestamp
    @Column(updatable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    public LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);

    @Column(nullable = false)
    public LocalDateTime lastUsedAt = LocalDateTime.now();

    @Column(nullable = false)
    public String deviceHint;

    @Column(nullable = false)
    public String ipAddress;

    @ManyToOne
    @JoinColumn
    public User user;

    public boolean isExpired() {
        return this.expiresAt.isBefore(LocalDateTime.now());
    }
}
