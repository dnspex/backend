package com.dnspex.security;

import io.sentry.SentryClient;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.interceptor.Interceptor;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import io.sentry.Sentry;

import static org.hibernate.engine.internal.StatisticalLoggingSessionEventListener.isLoggingEnabled;

@Slf4j
@Provider
@Priority(100) // Lower = runs earlier
public class ServerLogInterceptor  {

  /**
   * Logs incoming HTTP requests before they are processed by JAX-RS resources.
   * <p>
   * Captures request method, URI, headers, and body content. For requests with bodies, the body is read asynchronously
   * to avoid blocking the request processing.
   *
   * @param request
   *          the incoming HTTP request
   * @param requestContext
   *          the JAX-RS request context
   */


  @ServerRequestFilter(preMatching = true)
  public void logRequest(HttpServerRequest request, ContainerRequestContext requestContext) {
    String baseInfo = buildRequestBaseInfo(request);
    if (requestContext.hasEntity()) {
      request.body()
          .onSuccess(buffer -> logWithBody(baseInfo, buffer != null ? buffer.toString() : ""))
          .onFailure(cause -> {

            log.warn("{}, Body read failed: {}", baseInfo, cause.getMessage());
            Sentry.logger().warn(baseInfo + ", Body read failed: " + cause.getMessage(), SentryLevel.WARNING);
          });
    } else {
      logWithoutBody(baseInfo);
    }
  }

  /**
   * Logs outgoing HTTP responses after they are processed by JAX-RS resources.
   * <p>
   * Captures response status, headers, and body content.
   *
   * @param request
   *          the original HTTP request
   * @param responseContext
   *          the JAX-RS response context
   */
  @ServerResponseFilter
  public void logResponse(HttpServerRequest request, ContainerResponseContext responseContext) {
    String baseInfo = buildResponseBaseInfo(request, responseContext);
    if (responseContext.hasEntity()) {
      Object entity = responseContext.getEntity();
      String body = entity != null ? entity.toString() : "";
      logWithBody(baseInfo, body);
    } else {
      logWithoutBody(baseInfo);
    }
  }
  
  private String buildRequestBaseInfo(HttpServerRequest request) {
    return String.format("Request: %s %s Headers[%s]",
        request.method(),
        request.absoluteURI(),
        formatHeaders(request.headers()));
  }

  private Object formatHeaders(MultiMap headers) {
    return headers.entries().stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .reduce((a, b) -> a + ", " + b)
        .orElse("");
  }

  private String buildResponseBaseInfo(HttpServerRequest request, ContainerResponseContext responseContext) {
    return String.format("Response: %s %s, Status[%d %s], Headers[%s]",
        request.method(),
        request.absoluteURI(),
        responseContext.getStatus(),
        responseContext.getStatusInfo().getReasonPhrase(),
        formatHeaders(responseContext.getHeaders()));
  }

  private Object formatHeaders(MultivaluedMap<String, Object> headers) {
    return headers.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .reduce((a, b) -> a + ", " + b)
        .orElse("");
  }

  private void logWithBody(String baseInfo, String body) {
    log.info("{}, Body:\n{}", baseInfo, truncateBody(body));

    Sentry.logger().info(baseInfo + ", Body:\n " + truncateBody(body));
  }

  private Object truncateBody(String body) {
    return body.length() > 1000 ? body.substring(0, 1000) + "..." : body;
  }

  private void logWithoutBody(String baseInfo) {
    log.info("{}, Empty body", baseInfo);

    Sentry.logger().info(baseInfo + " " + "Empty body");
  }
}