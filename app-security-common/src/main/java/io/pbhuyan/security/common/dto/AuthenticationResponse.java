package io.pbhuyan.security.common.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationResponse(
        String token
) {
    static final String tokenType = "Bearer";
}
