package com.dnspex.util.rest.global;

import com.cuddoo.http.exception.HttpResponsePayload;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.error("Unerwarteter Systemfehler: ", exception);

        HttpResponsePayload payload = new HttpResponsePayload(
                "INTERNAL_SERVER_ERROR",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                null,
                LocalDateTime.now()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(payload)
                .build();
    }
}