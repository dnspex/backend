package com.dnspex.security.filter.session;

import com.dnspex.entity.auth.Session;
import com.dnspex.security.filter.HttpProtectionFilter;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@SessionProtection
@Priority(Priorities.AUTHENTICATION)
public class SessionProtectionFilter extends HttpProtectionFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) {
        Session session = this.get(context);

        context.setProperty("userId", session.getUser().getId());
        context.setProperty("sessionId", session.getId());
    }
}
