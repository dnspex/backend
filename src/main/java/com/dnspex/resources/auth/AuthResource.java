package com.dnspex.resources.auth;

import com.dnspex.dto.request.user.auth.*;
import com.dnspex.security.log.ExcludeBodyLogging;
import com.dnspex.service.auth.AuthService;
import com.dnspex.service.user.SessionService;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.security.Authenticated;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Context
    HttpServerRequest httpServerRequest;

    @Inject
    AuthService authService;

    @Inject
    SessionService sessionService;

    @POST
    @PermitAll
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/refresh")
    public Response refresh(@Valid @NotBlank(message = "TOKEN_REQUIRED") String token) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", this.sessionService.refresh(token));
    }

    @POST
    @PermitAll
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/validate")
    public Response validate(@Valid @NotBlank(message = "TOKEN_REQUIRED") String token) {
        if (this.sessionService.isExpired(token)) throw new HttpResponse(Response.Status.BAD_REQUEST, "INVALID_SESSION");
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

    @POST
    @PermitAll
    @ExcludeBodyLogging
    @Path("/login")
    public Response login(@Valid AuthLoginRequest request) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", this.authService.login(request,
                this.httpServerRequest.remoteAddress().toString(),
                this.httpServerRequest.getHeader("User-Agent")
        ));
    }

    @POST
    @PermitAll
    @ExcludeBodyLogging
    @Path("/register")
    public Response register(@Valid AuthRegisterRequest request) {
        this.authService.register(request);
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

    @DELETE
    @Authenticated
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/logout")
    public Response logout(@Valid @NotBlank(message = "REFRESH_TOKEN_REQUIRED") String token) {
        this.authService.logout(token);
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

    @POST
    @PermitAll
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/verify")
    public Response verify(@Valid @NotBlank(message = "TOKEN_REQUIRED") String token) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", this.authService.verify(token,
                this.httpServerRequest.remoteAddress().toString(),
                this.httpServerRequest.getHeader("User-Agent")
        ));
    }

    @POST
    @PermitAll
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/password-reset")
    public Response requestPasswordReset(@Valid @NotBlank(message = "EMAIL_REQUIRED")
                                             @Email(message = "EMAIL_INVALID")
                                             String email) {
        this.authService.requestPasswordReset(email);
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

     @POST
     @PermitAll
     @Path("/password-reset/confirm")
     public Response confirmPasswordReset(@Valid AuthResetRequest request) {
         this.authService.resetPassword(request);
         return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
     }
}
