package com.agenticIde.distributed_lovable.account_service.dto.auth;


// it creates a class which is immutable
public record AuthResponse(
        String token,
        UserProfileResponse user
) {
}
