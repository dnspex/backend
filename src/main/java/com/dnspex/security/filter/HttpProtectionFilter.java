package com.dnspex.security.filter;

import com.dnspex.entity.auth.Session;
import com.dnspex.service.user.SessionService;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class HttpProtectionFilter {

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    SessionService sessionService;

    public Session get(ContainerRequestContext context) {
        var authorization = context.getHeaderString("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            throw new HttpResponse(Response.Status.UNAUTHORIZED, "INVALID_TOKEN");
        }

        try {
            var sessionId = jsonWebToken.getClaim("sessionId").toString();

            Session session = this.sessionService.findById(sessionId);
            if (session.isExpired()) throw new HttpResponse(Response.Status.UNAUTHORIZED, "TOKEN_EXPIRED");

            return session;
        } catch (HttpResponse e) {
            context.abortWith(HttpResponse.send(Response.Status.fromStatusCode(e.getStatus()), e.getId()));
        } catch (Exception e) {
            context.abortWith(HttpResponse.send(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"));
        }

        return null;
    }
}
