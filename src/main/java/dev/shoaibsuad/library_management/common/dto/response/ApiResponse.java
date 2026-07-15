package dev.shoaibsuad.library_management.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Instant timestamp;
    private boolean success;
    private int status;
    private String message;
    private T data;
    private String path;
    private String details;
    private Object header;


    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .success(true)
                .status(200)
                .message("Operation successful")
                .data(data)
                .build();
    }


    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .success(true)
                .status(200)
                .message(message)
                .header(null)
                .build();
    }


    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .header(null)
                .build();
    }


    public static <T> ApiResponse<T> error(
            int status,
            String message,
            String details) {

        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .success(false)
                .status(status)
                .message(message)
                .details(details)
                .data(null)
                .header(null)
                .build();
    }
}