package com.dnspex.resources.domain;

import com.dnspex.dto.request.domain.DomainCreateRequest;
import com.dnspex.dto.response.domain.DomainCreateResponse;
import com.dnspex.service.domain.DomainService;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/domain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DomainResource {

    @Inject
    DomainService domainService;

    @POST
    @Authenticated
    @Path("/")
    public Response create(@Valid DomainCreateRequest request) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", DomainCreateResponse.of(domainService.create(request)));
    }
}
