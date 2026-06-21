package com.dnspex.entity.base;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@MappedSuperclass
public abstract class AbstractAuditingEntity extends PanacheEntityBase {

    @Column(updatable = false, nullable = false)
    public String createdBy;

    @Column(updatable = false, nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    public String lastModifiedBy;

    @Column(nullable = false)
    public LocalDateTime lastModifiedAt;
}
