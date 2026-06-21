package com.dnspex.security.filter;

import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION - 1)
@PreMatching
public class MePathResolverFilter implements ContainerRequestFilter {

    @Inject
    JsonWebToken jsonWebToken;

    @Override
    public void filter(ContainerRequestContext ctx) {
        var path = ctx.getUriInfo().getPath();
        var securityContext = ctx.getSecurityContext();
        var auth = ctx.getHeaderString("Authorization");

        if(path.contains("@me") && auth != null && auth.startsWith("Bearer "))  {
            if (jsonWebToken.getClaimNames() == null) {
                ctx.abortWith(HttpResponse.send(Response.Status.UNAUTHORIZED, "UNAUTHORIZED_NOT_AUTHENTICATED"));
                return;
            }

            URI baseUri = ctx.getUriInfo().getBaseUri();

            Principal principal = securityContext.getUserPrincipal();

            if (principal.getName().equals(jsonWebToken.getSubject())) {
                ctx.abortWith(HttpResponse.send(Response.Status.FORBIDDEN, "FORBIDDEN_NAME"));
                return;
            }

            String userId = jsonWebToken.getClaim("id").toString();

            var newPath = path.replace("@me", userId);
            URI newUri = UriBuilder.fromUri(baseUri).path(newPath).build();
            ctx.setRequestUri(newUri);

        } else if (path.contains("@me") && (auth == null || !auth.startsWith("Bearer "))) {
            ctx.abortWith(HttpResponse.send(Response.Status.UNAUTHORIZED, "UNAUTHORIZED_NO_TOKEN"));
        }
    }
}