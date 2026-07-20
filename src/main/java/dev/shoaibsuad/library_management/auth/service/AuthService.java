package dev.shoaibsuad.library_management.auth.service;

import dev.shoaibsuad.library_management.auth.dto.request.LoginRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RefreshTokenRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.AuthResponse;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    void logout(String bearerToken, RefreshTokenRequest request);
}
