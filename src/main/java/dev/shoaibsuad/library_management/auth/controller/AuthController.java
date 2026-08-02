package dev.shoaibsuad.library_management.auth.controller;

import dev.shoaibsuad.library_management.auth.dto.request.LoginRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RefreshTokenRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.AuthResponse;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;
import dev.shoaibsuad.library_management.auth.service.AuthService;
import dev.shoaibsuad.library_management.common.constants.ApiEndpoints;
import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiEndpoints.Auth.BASE_AUTH)
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Operations for user registration, login, token refresh, and logout"
)
public class AuthController {
    private final AuthService authService;
    @Operation(
            summary = "Register user",
            description = "Creates a new active user account with a BCrypt encrypted password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "User registration payload containing identity and credential details.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(
                                    name = "Register user",
                                    value = """
                                            {
                                              "firstName": "Saiful",
                                              "lastName": "Islam",
                                              "username": "Saiful",
                                              "email": "saiful@example.com",
                                              "phoneNumber": "+8801700000000",
                                              "password": "StrongPass123"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid registration payload",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Username or email already exists",
                            content = @Content
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse userResponse = authService.register(request);
        log.info("REGISTER REQUEST: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "User registered successfully",
                        userResponse
                ));
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates a user by username and password, then issues access and refresh tokens.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login payload containing username and password.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Login user",
                                    value = """
                                            {
                                              "username": "ocpsaifulislam",
                                              "password": "StrongPass123"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),"Login successful", authService.login(request)));
    }

    @Operation(
            summary = "Refresh token",
            description = "Validates a persisted refresh token, revokes it, and returns a newly rotated token pair.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Refresh payload containing the refresh token.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(
                                    name = "Refresh token",
                                    value = """
                                            {
                                              "refreshToken": "raw-refresh-token"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Token refreshed successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Refresh token is invalid, expired, or revoked",
                            content = @Content
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),"Token refreshed successfully", authService.refresh(request)));
    }

    @Operation(
            summary = "Logout user",
            description = "Revokes the submitted refresh token and blacklists the active access token.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Logout payload containing the refresh token to revoke.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(
                                    name = "Logout user",
                                    value = """
                                            {
                                              "refreshToken": "raw-refresh-token"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Logout successful",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Access token or refresh token is invalid",
                            content = @Content
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(description = "Bearer access token.", required = true)
            @RequestHeader(name = "Authorization") String authorization,
            @Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(authorization, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),"Logout successful"));
    }

}