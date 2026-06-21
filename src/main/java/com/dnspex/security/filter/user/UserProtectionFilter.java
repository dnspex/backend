
package com.dnspex.security.filter.user;

import com.dnspex.entity.auth.Session;
import com.dnspex.security.filter.HttpProtectionFilter;
import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@UserProtection
@Priority(Priorities.AUTHENTICATION)
public class UserProtectionFilter extends HttpProtectionFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        try {
            Session customerSession = this.get(context);

            MultivaluedMap<String, String> pathParameter = context.getUriInfo().getPathParameters();
            if(!pathParameter.containsKey("accountId")) throw new HttpResponse(Response.Status.NOT_IMPLEMENTED,  "USER_ID_REQUIRED");

            var sessionId = customerSession.getId();
            var userId = customerSession.getUser().getId().toString();
            var pathUserId = pathParameter.getFirst("userId");

            if(pathUserId.isBlank() || !pathUserId.equals(userId)) throw new HttpResponse(Response.Status.FORBIDDEN, "FORBIDDEN");

            context.setProperty("sessionId", sessionId);
            context.setProperty("userId", userId);
        } catch (HttpResponse e) {
            context.abortWith(HttpResponse.send(Response.Status.fromStatusCode(e.getStatus()), e.getId()));
        }
    }
}
