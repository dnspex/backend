package com.dnspex.resources.dns;

import com.dnspex.service.dns.DnsService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/dns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DnsResource {

    @Inject
    DnsService dnsService;

    @GET
    @PermitAll
    @Path("/")
    public Response dns() {
        dnsService.dns();
        return Response.ok().build();
    }
}
