package com.dnspex.service.dns;

import com.dnspex.service.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

@ApplicationScoped
public class DnsService {

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    UserService userService;

    public void dns() {
        try {
            Record[] records = new Lookup("cuddoo.com", Type.A, DClass.IN).run();
            for (Record record : records) {
                System.out.println(record.getName());
                System.out.println(record.rdataToString());
            }
        } catch (TextParseException e) {
            throw new RuntimeException(e);
        }
    }
}