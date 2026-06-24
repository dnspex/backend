package com.dnspex.security.log.filter;

import com.dnspex.security.log.ExcludeBodyLogging;
import com.dnspex.security.log.SentryLogger;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

@Priority(300)
public class SentryResponseFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    SentryLogger trafficLogger;

    /**
     * Logs outgoing responses after resource processing.
     * <p>
     * {@link ExcludeBodyLogging} on the matched resource method suppresses body content.
     */
    @ServerResponseFilter
    public void filter(HttpServerRequest request, ContainerResponseContext responseContext) {
        Object entity = responseContext.hasEntity() ? responseContext.getEntity() : null;
        String body   = trafficLogger.resolveBody(entity != null ? entity.toString() : null, resourceInfo);

        trafficLogger.logResponse(request, responseContext, body);
    }
}
