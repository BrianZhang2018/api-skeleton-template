package com.example.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class ApiResponse<T> {
    @JsonProperty("success")
    private final boolean success;

    @JsonProperty("data")
    private final T data;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("timestamp")
    private final Instant timestamp;

    private ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
