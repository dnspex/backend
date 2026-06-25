package com.dnspex.entity.monitor;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.entity.user.User;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<MonitorRecord> records = new ArrayList<>();
}
