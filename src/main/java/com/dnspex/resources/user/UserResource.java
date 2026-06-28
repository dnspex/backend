package com.dnspex.resources.user;

import com.dnspex.dto.response.user.UserPrivateResponse;
import com.dnspex.dto.response.user.UserPublicResponse;
import com.dnspex.entity.user.User;
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
        User sessionOwner = userService.get();

        User user = userService.findByIdAndActive(id);
        if (!user.getId().equals(sessionOwner.getId())) return Response.ok(UserPublicResponse.of(user)).build();

        return Response.ok(UserPrivateResponse.of(user)).build();
    }
}
