package com.dnspex.entity.domain;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.DomainState;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "domain")
@EntityListeners(AuditingEntityListener.class)
public class Domain extends AbstractAuditingEntity {

    @Id
    public String id = IdentifierManager.generate("domain");

    @Column(nullable = false, unique = true)
    public String name;

    @ManyToOne
    @JoinColumn
    public User user;

    @Column(nullable = false, unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public DomainState state = DomainState.PENDING;

    @ElementCollection
    @CollectionTable(name = "domain_updates", joinColumns = @JoinColumn(name = "domain_id"))
    @Column(name = "update")
    public Set<String> updates = Set.of();

    public LocalDateTime verifiedAt = null; //ToDO: scheduler update
}
