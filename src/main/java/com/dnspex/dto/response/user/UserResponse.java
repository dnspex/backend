package com.dnspex.dto.response.user;

public sealed interface UserResponse
        permits UserPrivateResponse, UserPublicResponse {}
