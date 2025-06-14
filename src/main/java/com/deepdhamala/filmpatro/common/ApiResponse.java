package com.deepdhamala.filmpatro.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;
    private List<ApiError> errors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApiError{
        private String field;
        private String message;
    }

    public static <T> ApiResponse<T> success(T data, String message){
        return new ApiResponse<>("success", 200, message, data, null);
    }

    public static <T> ApiResponse<T> error(int code, String message, List<ApiError> errors){
        return new ApiResponse<>("error", code, message, null, errors);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>("error", code, message, null, null);
    }
}
