package com.dnspex.resources.domain;

import com.dnspex.dto.request.domain.DomainCreateRequest;
import com.dnspex.service.domain.DomainService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/domain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DomainResource {

    @Inject
    DomainService domainService;

    @POST
    @PermitAll
    @Path("/")
    public Response create(@Valid DomainCreateRequest request) {
        return Response.ok(domainService.create(request)).build();
    }
}
