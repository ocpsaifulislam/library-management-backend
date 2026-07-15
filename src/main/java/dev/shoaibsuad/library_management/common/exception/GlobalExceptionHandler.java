package dev.shoaibsuad.library_management.common.exception;

import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception,
                                                                       HttpServletRequest request) {
//        String message = exception.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//        return problemDetail(HttpStatus.BAD_REQUEST, "Validation failed", message);

        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                exception.getMessage()
        );

        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException exception) {
        return problemDetail(HttpStatus.NOT_FOUND, "Resource not found", exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceConflictException(
            ResourceConflictException exception,
            HttpServletRequest request) {

        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.CONFLICT.value(),
                "Resource conflict",
                exception.getMessage()
        );

        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ignoredException) {
        return problemDetail(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid username or password.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ignoredException) {
        return problemDetail(HttpStatus.FORBIDDEN, "Access denied", "You do not have permission to perform this action.");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidTokenException(InvalidTokenException exception) {
        return problemDetail(HttpStatus.UNAUTHORIZED, "Invalid token", exception.getMessage());
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, OptimisticLockException.class})
    public ProblemDetail handleOptimisticLockingException(RuntimeException ignoredException) {
        return problemDetail(
                HttpStatus.CONFLICT,
                "Concurrent update conflict",
                "Inventory was updated by another request. Please retry with the latest state.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Exception occurred", ex);

        String message = extractDatabaseMessage(ex);

        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                message
        );

        response.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
    private ProblemDetail problemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
    private String extractDatabaseMessage(Throwable ex) {

        Throwable cause = ex;

        while (cause != null) {

            String message = cause.getMessage();

            if (message != null && message.contains("ORA-")) {

                int start = message.indexOf("ORA-");

                int end = message.indexOf("\n", start);

                if (end == -1) {
                    end = message.length();
                }

                return message.substring(start, end).trim();
            }

            cause = cause.getCause();
        }

        return ex != null && ex.getMessage() != null
                ? ex.getMessage()
                : "Unexpected database error occurred";
    }
}
