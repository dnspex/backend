package com.dnspex.util.rest;

import java.time.LocalDateTime;

public record HttpResponsePayload(
        String id,
        Integer status,
        Object data,
        LocalDateTime timestamp
) { }