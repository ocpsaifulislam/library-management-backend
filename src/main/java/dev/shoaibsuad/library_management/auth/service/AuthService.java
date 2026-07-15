package dev.shoaibsuad.library_management.auth.service;

import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
}
