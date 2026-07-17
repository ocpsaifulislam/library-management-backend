package dev.shoaibsuad.library_management.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);

    /**
     * Extracts the username subject from a JWT.
     *
     * @param token JWT value
     * @return username subject
     */
    String extractUsername(String token);

    /**
     * Validates a token against user details, signature, and expiration.
     *
     * @param token JWT value
     * @param userDetails expected user details
     * @return {@code true} when the token is valid
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Calculates the remaining lifetime of a token.
     *
     * @param token JWT value
     * @return remaining token lifetime
     */
    Duration getRemainingLifetime(String token);

    /**
     * Returns the configured access token expiration.
     *
     * @return access token lifetime
     */
    Duration getAccessTokenExpiration();
}
