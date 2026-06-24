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
    @Path("/refresh")
    public Response refresh(@Valid @NotBlank String token) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", sessionService.refresh(token));
    }

    @POST
    @PermitAll
    @ExcludeBodyLogging
    @Path("/login")
    public Response login(@Valid AuthLoginRequest request) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", authService.login(request,
                httpServerRequest.remoteAddress().toString(),
                httpServerRequest.getHeader("User-Agent")
        ));
    }

    @POST
    @ExcludeBodyLogging
    @Path("/register")
    public Response register(@Valid AuthRegisterRequest request) {
        authService.register(request);
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

    @DELETE
    @Authenticated
    @Path("/logout")
    public Response logout(@Valid AuthLogoutRequest request) {
        authService.logout(request.refreshToken());
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

    @POST
    @PermitAll
    @Path("/verify")
    public Response verify(@Valid AuthVerifyRequest request) {
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY", authService.verify(request,
                httpServerRequest.remoteAddress().toString(),
                httpServerRequest.getHeader("User-Agent")
        ));
    }

    @POST
    @PermitAll
    @Path("/password-reset")
    public Response requestPasswordReset(@Valid AuthSendResetRequest request) {
        authService.requestPasswordReset(request.email());
        return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
    }

     @POST
     @PermitAll
     @Path("/password-reset/confirm")
     public Response confirmPasswordReset(@Valid AuthResetRequest request) {
         authService.resetPassword(request);
         return HttpResponse.send(Response.Status.OK, "SUCCESSFULLY");
     }
}
