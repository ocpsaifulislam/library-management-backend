package dev.shoaibsuad.library_management.auth.service.impl;

import dev.shoaibsuad.library_management.auth.config.JwtProperties;
import dev.shoaibsuad.library_management.auth.dto.request.LoginRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RefreshTokenRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.AuthResponse;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;
import dev.shoaibsuad.library_management.auth.entity.RefreshToken;
import dev.shoaibsuad.library_management.auth.entity.User;
import dev.shoaibsuad.library_management.auth.mapper.UserMapper;
import dev.shoaibsuad.library_management.auth.repository.RefreshTokenRepository;
import dev.shoaibsuad.library_management.auth.repository.UserRepository;
import dev.shoaibsuad.library_management.auth.service.AuthService;
import dev.shoaibsuad.library_management.auth.service.JwtService;
import dev.shoaibsuad.library_management.auth.service.TokenBlacklistService;
import dev.shoaibsuad.library_management.common.exception.InvalidTokenException;
import dev.shoaibsuad.library_management.common.exception.ResourceConflictException;
import dev.shoaibsuad.library_management.common.service.IdGeneratorService;
import dev.shoaibsuad.library_management.auth.util.TokenHashUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl  implements AuthService {
    private static final String TOKEN_TYPE = "Bearer";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int REFRESH_TOKEN_BYTES = 64;

    private final IdGeneratorService idGeneratorService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResourceConflictException("User with username '" + request.username() + "' already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("User with email '" + request.email() + "' already exists.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setId(idGeneratorService.getNewId("USERS"));
        user.setIsActive(1L);

        entityManager.persist(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Attempting to authenticate user: {}", request.username());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            log.error("Authentication failed inside Spring Security!");
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Failure reason message: {}", e.getMessage());
            throw e;
        }
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.username()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        System.out.println(userDetails);
        String accessToken = jwtService.generateAccessToken(userDetails);
        System.out.println(accessToken);
        String refreshToken = createRefreshToken(user);
        System.out.println(refreshToken);

        log.info("User logged in: {}", user.getUsername());
        return authResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = getValidRefreshToken(request.refreshToken());
        refreshToken.setRevoked(1L);
        refreshTokenRepository.save(refreshToken);

        User user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = createRefreshToken(user);
        return authResponse(accessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String bearerToken, RefreshTokenRequest request) {
        String accessToken = extractBearerToken(bearerToken);
        Duration ttl;
        try {
            ttl = jwtService.getRemainingLifetime(accessToken);
        } catch (RuntimeException exception) {
            throw new InvalidTokenException("Access token is invalid.");
        }
        tokenBlacklistService.blacklist(accessToken, ttl);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(TokenHashUtil.sha256(request.refreshToken()))
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid."));
        refreshToken.setRevoked(1L);
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken getValidRefreshToken(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(TokenHashUtil.sha256(rawToken))
                .orElseThrow(() -> new InvalidTokenException("Refresh token is invalid."));

        if (refreshToken.getRevoked() != null && refreshToken.getRevoked().equals(1L)) {
            throw new InvalidTokenException("Refresh token has been revoked.");
        }
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshToken.setRevoked(1L);
            refreshTokenRepository.save(refreshToken);
            throw new InvalidTokenException("Refresh token has expired.");
        }
        if (refreshToken.getUser() == null ||
                refreshToken.getUser().getIsActive() == null ||
                refreshToken.getUser().getIsActive().equals(0L)) {
            throw new InvalidTokenException("User account is inactive.");
        }

        return refreshToken;
    }

    private String createRefreshToken(User user) {
        String rawToken = generateSecureToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setId(idGeneratorService.getNewId("REFRESH_TOKENS"));
        refreshToken.setTokenHash(TokenHashUtil.sha256(rawToken));
        refreshToken.setExpiresAt(LocalDateTime.now().plus(jwtProperties.refreshExpiration()));
        refreshToken.setRevoked(0L);
        entityManager.persist(refreshToken);
        return rawToken;
    }

    private AuthResponse authResponse(String accessToken, String refreshToken) {
        return new AuthResponse(
                accessToken,
                refreshToken,
                TOKEN_TYPE,
                jwtService.getAccessTokenExpiration().toSeconds());
    }

    private String generateSecureToken() {
        byte[] tokenBytes = new byte[REFRESH_TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private String extractBearerToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            throw new InvalidTokenException("Bearer access token is required.");
        }
        return bearerToken.substring(BEARER_PREFIX.length());
    }
}
