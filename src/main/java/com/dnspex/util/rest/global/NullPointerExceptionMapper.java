package com.dnspex.util.rest.global;

import com.dnspex.util.rest.HttpResponsePayload;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {

    @Override
    public Response toResponse(NullPointerException exception) {
        HttpResponsePayload payload = new HttpResponsePayload(
                "MISSING_BODY",
                Response.Status.BAD_REQUEST.getStatusCode(),
                null,
                java.time.LocalDateTime.now()
        );

        return Response.status(Response.Status.BAD_REQUEST).entity(payload).build();
    }
}
