package com.dnspex.resources.user;

import com.dnspex.dto.response.user.UserPrivateResponse;
import com.dnspex.dto.response.user.UserPublicResponse;
import com.dnspex.entity.user.User;
import com.dnspex.service.user.UserService;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/user/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Authenticated
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        User sessionOwner = userService.get();

        User user = userService.findByIdAndActive(id);
        if (!user.getId().equals(sessionOwner.getId())) return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", UserPublicResponse.of(user));

        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", UserPrivateResponse.of(user));
    }

    @DELETE
    @Authenticated
    @Path("/")
    public Response delete() {
        this.userService.delete();
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }
}
