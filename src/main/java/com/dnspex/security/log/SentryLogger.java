package com.dnspex.security.log;

import io.quarkus.runtime.LaunchMode;
import io.sentry.*;
import io.sentry.logger.SentryLogParameters;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class SentryLogger {

  String profile = LaunchMode.current().name();

  private static final int    MAX_BODY_LENGTH = 1000;
  private static final String BODY_EMPTY      = "<empty>";
  private static final String BODY_EXCLUDED   = "<excluded by security>";

  SentryLogger() {
    Sentry.init(options -> {
      options.setDsn("https://042733524e069c186383eed615d15420@o4511598016004096.ingest.de.sentry.io/4511598029832272");
      options.getLogs().setEnabled(true);
      options.setEnvironment(this.profile.toLowerCase());
      options.addInAppExclude("*");
    });
  }

  public void logRequest(String method, String uri, String headers, String body, ContainerRequestContext requestContext) {
    String msg = String.format("─► REQUEST  %s %s", method, uri);

    log.info(msg);

    String uid = requestContext.getProperty("uid") != null ? requestContext.getProperty("uid").toString() : "unknown";
    String sid = requestContext.getProperty("sid") != null ? requestContext.getProperty("sid").toString() : "unknown";

    Sentry.logger().log(SentryLogLevel.INFO, SentryLogParameters.create(
                    SentryAttributes.of(
                            SentryAttribute.stringAttribute("method", method),
                            SentryAttribute.stringAttribute("uri", uri),
                            SentryAttribute.stringAttribute("headers", headers),
                            SentryAttribute.stringAttribute("body", body),
                            SentryAttribute.stringAttribute("uid", uid),
                            SentryAttribute.stringAttribute("sid", sid)
                    )),
            msg
    );
  }

  public void logRequestBodyReadFailed(String method, String uri, String headers, String cause) {
    String msg = String.format("─► REQUEST  %s %s%n   Headers : %s%n   Body    : <read failed: %s>",
            method, uri, headers, cause);
    log.warn(msg);
    Sentry.logger().warn(msg, SentryLevel.WARNING);
  }

  public void logResponse(HttpServerRequest request, ContainerResponseContext responseContext, String body) {
    String method       = request.method().name();
    String uri          = request.absoluteURI();
    int    status       = responseContext.getStatus();
    String statusPhrase = responseContext.getStatusInfo().getReasonPhrase();
    String headers      = this.formatResponseHeaders(responseContext.getHeaders());

    String msg = String.format("◄─ RESPONSE %d %s %s %s", status, statusPhrase, method, uri);
    log.info(msg);

    Sentry.logger().log(SentryLogLevel.INFO, SentryLogParameters.create(
            SentryAttributes.of(
                    SentryAttribute.integerAttribute("status", status),
                    SentryAttribute.stringAttribute("statusPhrase", statusPhrase),
                    SentryAttribute.stringAttribute("method", method),
                    SentryAttribute.stringAttribute("uri", uri),
                    SentryAttribute.stringAttribute("headers", headers),
                    SentryAttribute.stringAttribute("body", body)
            )),
            msg
    );
  }

  public String formatRequestHeaders(MultiMap headers) {
    return headers.entries().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(" | "));
  }

  public String formatResponseHeaders(MultivaluedMap<String, Object> headers) {
    return headers.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(" | "));
  }

  /**
   * Resolves the body string for logging. Returns a placeholder when the body
   * is empty or excluded via {@link ExcludeBodyLogging}, and truncates otherwise.
   * <p>
   * {@code resourceInfo} may be {@code null} in pre-matched request filters –
   * the annotation check is skipped in that case.
   */
  public String resolveBody(String raw, ResourceInfo resourceInfo) {
    if (raw == null || raw.isBlank()) return BODY_EMPTY;

    Method method = resourceInfo.getResourceMethod();
    if (method != null && method.isAnnotationPresent(ExcludeBodyLogging.class)) return BODY_EXCLUDED;

    return raw.length() > MAX_BODY_LENGTH
            ? raw.substring(0, MAX_BODY_LENGTH) + "…"
            : raw;
  }
}