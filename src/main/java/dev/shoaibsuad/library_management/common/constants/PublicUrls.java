package dev.shoaibsuad.library_management.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicUrls {
    /**
     * Endpoints that do not require authentication.
     */
    public static final String[] PUBLIC_ENDPOINTS = {
            ApiEndpoints.Auth.BASE_AUTH + "/register",
            ApiEndpoints.Auth.BASE_AUTH + "/login",
            ApiEndpoints.Auth.BASE_AUTH + "/refresh",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
}
