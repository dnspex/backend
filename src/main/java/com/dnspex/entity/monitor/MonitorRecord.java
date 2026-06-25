package com.dnspex.entity.monitor;

import com.dnspex.entity.base.AbstractAuditingEntity;
import com.dnspex.entity.listener.AuditingEntityListener;
import com.dnspex.util.math.IdentifierManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.xbill.DNS.Type;

@Setter
@Getter
@Entity
@Table(name = "monitor_record")
@EntityListeners(AuditingEntityListener.class)
public class MonitorRecord extends AbstractAuditingEntity {

    @Id
    private String id = IdentifierManager.generate("monrec");

    @ManyToOne
    @JoinColumn
    private Monitor monitor;

    public String domainName;

    public String tld;

    public int type = Type.A;
}
