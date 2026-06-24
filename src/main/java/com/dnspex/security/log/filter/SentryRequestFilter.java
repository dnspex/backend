package com.dnspex.security.log.filter;

import com.dnspex.security.log.SentryLogger;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

@Priority(400)
public class SentryRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    SentryLogger trafficLogger;


    @ServerRequestFilter
    public void log(HttpServerRequest request, ContainerRequestContext requestContext) {
        String method  = request.method().name();
        String uri     = request.absoluteURI();

        MultiMap reqHeaders = request.headers();
        reqHeaders.remove("Authorization");

        String headers = trafficLogger.formatRequestHeaders(reqHeaders);

        if (!requestContext.hasEntity()) {
            trafficLogger.logRequest(method, uri, headers, trafficLogger.resolveBody(null, resourceInfo), requestContext);
            return;
        }

        request.body()
                .onSuccess(buffer -> {
                    String raw  = buffer != null ? buffer.toString() : "";
                    String body = trafficLogger.resolveBody(raw, resourceInfo);
                    trafficLogger.logRequest(method, uri, headers, body, requestContext);
                })
                .onFailure(cause ->
                        trafficLogger.logRequestBodyReadFailed(method, uri, headers, cause.getMessage())
                );
    }
}
