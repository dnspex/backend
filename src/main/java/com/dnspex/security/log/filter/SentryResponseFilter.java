package com.dnspex.security.log.filter;

import com.dnspex.security.log.SentryLogger;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

@Priority(Priorities.HEADER_DECORATOR - 100)
public class SentryResponseFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    SentryLogger trafficLogger;

    @ServerResponseFilter
    public void filter(HttpServerRequest request, ContainerResponseContext responseContext) {
        Object entity = responseContext.hasEntity() ? responseContext.getEntity() : null;
        String body   = trafficLogger.resolveBody(entity != null ? entity.toString() : null, resourceInfo);

        trafficLogger.logResponse(request, responseContext, body);
    }
}
