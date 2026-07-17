package dev.shoaibsuad.library_management.common.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private LocalDateTime  timestamp;
    private boolean success;
    private int status;
    private String message;
    private T body;
    private T header;
    private String details;
    private String instance;

    public static <T> ApiResponse<T> success(int status,T body) {
        return new ApiResponse<>(LocalDateTime.now(),true,status, "Operation successful", body,null,null,null);
    }

    public static <T> ApiResponse<T> success(int status,String message) {
        return new ApiResponse<>(LocalDateTime.now(),true,status, message, null,null,null,null);
    }

    public static <T> ApiResponse<T> success(int status,String message, T body) {
        return new ApiResponse<>(LocalDateTime.now(),true,status, message, body,null,null,null);
    }

    public static <T> ApiResponse<T> error(int status,String message) {
        return new ApiResponse<>(LocalDateTime.now(),false, status,message,null, null,null,null);
    }
}