package com.dnspex.entity.monitor;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.entity.user.User;
import com.dnspex.util.enumeration.MonitorState;
import com.dnspex.util.enumeration.MonitorStatus;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "monitor")
@EntityListeners(AuditingEntityListener.class)
public class Monitor extends AbstractAuditingEntity {

    @Id
    public String id = IdentifierManager.generate("mon");

    @ManyToOne
    @JoinColumn
    public User user;

    @Column(nullable = false)
    public String name;

    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MonitorStatus status = MonitorStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public MonitorState state = MonitorState.PAUSED;

    @Column(nullable = false)
    public String domainName;

    @Column(nullable = false)
    public Integer domainType;

    @Column(nullable = false)
    public String value;
}
