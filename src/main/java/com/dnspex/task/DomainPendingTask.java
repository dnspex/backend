package com.dnspex.task;

import com.dnspex.entity.domain.Domain;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.UnknownHostException;
import java.util.List;

@ApplicationScoped
public class DomainPendingTask {

    @ConfigProperty(name = "dnspex.domain.validation.prefix")
    String prefix;

    @Scheduled(cron = "{dnspex.scheduled.domain.validation.interval}", timeZone = "{dnspex.scheduled.default.timezone}")
    void validate() {
        //check all domains of the txt record and when entry doesnt exist, then set state to DomainState.NOT_FOUND
        //when a entry found but the token is mismatch then set state to DomainState.MISMATCH

        List<Domain> domains = Domain.listAll();
        domains.forEach(domain -> {
            if (domain.isVerified()) return;

            try {
                Resolver resolver = new SimpleResolver("1.1.1.1");

                Lookup lookup = new Lookup(this.prefix + domain.getName(), Type.TXT);
                lookup.setResolver(resolver);

                Record[] records = lookup.run();

                System.out.println("====================================");
                if (records == null) {
                    System.out.println("Record not found for domain: " + domain.getName());
                    return;
                }

                Record record = records[0];
                if (record == null) {
                    System.out.println("Record not found for domain: " + domain.getName());
                }

                if (record != null) {
                    System.out.println("Domain: " + domain.getName());
                    String value = record.rdataToString();
                    System.out.println("TXT Value: " + value);

                    if (value.contains(domain.getToken())) {
                        System.out.println("Token matched for domain: " + domain.getName());
                    }

                    /*if (record.getName().equals(domain.getName())) {
                        String txtValue = record.rdataToString();
                        if (txtValue.contains(domain.getToken())) {
                            domain.setState(DomainState.VERIFIED);
                            domain.persist();
                        } else {
                            domain.setState(DomainState.MISMATCH);
                            domain.persist();
                        }
                    }*/
                    System.out.println("====================================");
                }
            } catch (TextParseException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
