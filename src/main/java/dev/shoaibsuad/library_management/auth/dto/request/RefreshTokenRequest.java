package dev.shoaibsuad.library_management.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload containing a refresh token credential.
 *
 * @author Saiful Islam
 */
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {
}
