package com.agenticIde.distributed_lovable.account_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank
        @Email
        String username,
        @NotBlank
        String name,
        @NotBlank
        @Size(min=4,max=50)
        String password
) {

}
