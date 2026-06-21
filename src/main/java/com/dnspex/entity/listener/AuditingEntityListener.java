package com.dnspex.entity.listener;

import com.dnspex.entity.base.AbstractAuditingEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.eclipse.microprofile.jwt.Claim;

import java.time.LocalDateTime;

@RequestScoped
public class AuditingEntityListener {

    @Claim("id")
    String id;

    @PrePersist
    public void prePersist(AbstractAuditingEntity entity) {
        entity.createdBy = id == null ? "system" : id;
        entity.lastModifiedBy = id == null ? "system" : id;
        entity.lastModifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(AbstractAuditingEntity entity) {
        entity.lastModifiedBy = id == null ? "system" : id;
        entity.lastModifiedAt = LocalDateTime.now();
    }
}
