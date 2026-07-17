package dev.shoaibsuad.library_management.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

/**
 * Configuration properties for JWT access and refresh token lifetimes.
 *
 * @author Saiful Islam
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String signingKey,
        Duration expiration,
        Duration refreshExpiration
) {
}
