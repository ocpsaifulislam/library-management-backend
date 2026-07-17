package dev.shoaibsuad.library_management.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record UserResponse(
        @JsonIgnore
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        Long isActive,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long createdBy,
        Long modifiedBy
) {
}
