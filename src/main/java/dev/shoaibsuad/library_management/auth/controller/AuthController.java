package dev.shoaibsuad.library_management.auth.controller;

import dev.shoaibsuad.library_management.auth.dto.request.LoginRequest;
import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.AuthResponse;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;
import dev.shoaibsuad.library_management.auth.service.AuthService;
import dev.shoaibsuad.library_management.common.constants.ApiEndpoints;
import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}