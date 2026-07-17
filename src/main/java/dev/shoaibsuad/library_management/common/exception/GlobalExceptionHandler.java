package dev.shoaibsuad.library_management.common.exception;

import dev.shoaibsuad.library_management.common.dto.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles request validation errors.
     *
     * @param exception validation exception
     * @return bad request response with validation messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return problemDetail(HttpStatus.BAD_REQUEST, "Validation failed", message);
    }

    /**
     * Handles missing persistent resources.
     *
     * @param exception not found exception
     * @return not found response
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException exception) {
        return problemDetail(HttpStatus.NOT_FOUND, "Resource not found", exception.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessException(
            DataAccessException exception, HttpServletRequest request) {

        log.error("A database error occurred {}", request.getRequestURI(), exception);
        String oracleMessage = extractOracleErrorMessage(exception);
        ApiResponse<Void> body = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                oracleMessage
        );

        body.setDetails("A database error occurred");
        body.setInstance(request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception exception, HttpServletRequest request) {
        ApiResponse<Void> body = ApiResponse.error(
                HttpStatus.CONFLICT.value(),
                exception.getMessage()
        );
        body.setDetails("Internal server error");
        body.setInstance(request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceConflictException(
            ResourceConflictException exception,
            HttpServletRequest request) {
        ApiResponse<Void> body = ApiResponse.error(
                HttpStatus.CONFLICT.value(),
                exception.getMessage()
        );

        body.setDetails("Resource conflict");
        body.setInstance(request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Handles authentication credential failures.
     *
     * @param exception authentication exception
     * @return unauthorized response
     */
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception) {
        return problemDetail(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid username or password.");
    }

    /**
     * Handles authorization failures from method-level security checks.
     *
     * @param exception access denied exception
     * @return forbidden response
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception) {
        return problemDetail(HttpStatus.FORBIDDEN, "Access denied", "You do not have permission to perform this action.");
    }

    /**
     * Handles invalid, expired, or revoked authentication tokens.
     *
     * @param exception invalid token exception
     * @return unauthorized response
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidTokenException(InvalidTokenException exception) {
        return problemDetail(HttpStatus.UNAUTHORIZED, "Invalid token", exception.getMessage());
    }

    @ExceptionHandler({OptimisticLockingFailureException.class, OptimisticLockException.class})
    public ProblemDetail handleOptimisticLockingException(RuntimeException exception) {
        return problemDetail(
                HttpStatus.CONFLICT,
                "Concurrent update conflict",
                "Inventory was updated by another request. Please retry with the latest state.");
    }

    private ProblemDetail problemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
    private String extractOracleErrorMessage(Throwable exception) {
        // Walk to the root cause, since Hibernate/Spring wrap the raw SQLException
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        String rawMessage = rootCause.getMessage();
        if (rawMessage == null) {
            return "A record with this information already exists.";
        }

        Matcher matcher = Pattern.compile("(ORA-\\d{5}:[^\\n]*?)(?=\\n|$)").matcher(rawMessage);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "A record with this information already exists.";
    }
}
