package io.pbhuyan.security.common.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "User name is required") String username,
        @NotBlank(message = "Password is required") String password) {
}
