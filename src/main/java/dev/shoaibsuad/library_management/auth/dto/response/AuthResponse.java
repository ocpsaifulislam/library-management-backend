package dev.shoaibsuad.library_management.auth.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn

) {
}
