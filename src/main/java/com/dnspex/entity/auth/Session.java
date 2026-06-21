package com.dnspex.entity.auth;

import com.dnspex.entity.user.User;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "sessions")
@Entity
public class Session extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id = UUID.randomUUID();

    @Column(unique = true)
    public String refreshToken = UUID.randomUUID().toString(); //replace it with real token like jwt

    @CreationTimestamp
    @Column(updatable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    public LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

    @Column(nullable = false)
    public LocalDateTime lastUsedAt = LocalDateTime.now();

    public String deviceHint;
    public String ipAddress;

    @ManyToOne
    @JoinColumn
    public User user;

    public boolean isExpired() {
        return this.expiresAt.isBefore(LocalDateTime.now());
    }
}
