package com.dnspex.util.rest;

import com.dnspex.util.rest.exception.HttpResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class HttpResponseHandler implements ExceptionMapper<HttpResponse> {

    @Override
    public Response toResponse(HttpResponse response) {
        HttpResponsePayload payload = new HttpResponsePayload(
                response.getId(),
                response.getStatus(),
                response.getData(),
                LocalDateTime.now()
        );

        return Response.status(response.getStatus()).entity(payload).build();
    }
}
