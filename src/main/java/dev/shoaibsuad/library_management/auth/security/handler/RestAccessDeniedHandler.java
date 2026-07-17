package dev.shoaibsuad.library_management.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Writes JSON responses when authenticated users lack required access.
 *
 * @author Saiful Islam
 */
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    /**
     * Handles authorization failures using the API response shape.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param accessDeniedException access denied exception
     * @throws IOException when the response cannot be written
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<Void> apiResponse = ApiResponse.error(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
