package com.dnspex.resources.user;

import com.dnspex.service.user.UserService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/user/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Authenticated
    @Path("/")
    public Response get(@PathParam("id") String id) {
        return Response.ok(userService.get(id)).build();
    }
}
