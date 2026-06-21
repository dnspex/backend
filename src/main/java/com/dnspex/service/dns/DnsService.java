package com.dnspex.service.dns;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class DnsService {

    @Inject
    JsonWebToken jsonWebToken;

    public void test() {

    }
}
