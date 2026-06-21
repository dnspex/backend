package com.dnspex.security.filter;

import com.dnspex.entity.auth.Session;
import com.dnspex.service.user.SessionService;
import com.dnspex.util.rest.exception.HttpResponse;
import io.quarkus.security.Authenticated;
import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.lang.reflect.Method;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SessionProtectionFilter implements ContainerRequestFilter {

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    SessionService sessionService;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext context) {
        var authorization = context.getHeaderString("Authorization");

        Method method = resourceInfo.getResourceMethod();

        boolean requiresAuth = method.isAnnotationPresent(Authenticated.class) || method.isAnnotationPresent(RolesAllowed.class);
        if (!requiresAuth) return;

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new HttpResponse(Response.Status.UNAUTHORIZED, "INVALID_TOKEN");
        }

        if (this.jsonWebToken.getClaimNames() == null) throw new HttpResponse(Response.Status.UNAUTHORIZED, "NOT_AUTHENTICATED");

        var sessionId = this.jsonWebToken.getClaim("sid").toString();

        Session session = this.sessionService.findById(sessionId);
        if (session.isExpired()) throw new HttpResponse(Response.Status.UNAUTHORIZED, "TOKEN_EXPIRED");

        //context.setProperty("userId", session.getUser().getId()); we use jwt claims instead of session properties to avoid db calls in other filters and resources
        //context.setProperty("sessionId", session.getId());
    }
}
