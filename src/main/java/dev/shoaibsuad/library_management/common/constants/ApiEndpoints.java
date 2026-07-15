package dev.shoaibsuad.library_management.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {
    private static final String API_VERSION = "/api/v1";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Auth {
        public static final String BASE_AUTH = API_VERSION + "/auth";
    }

}
