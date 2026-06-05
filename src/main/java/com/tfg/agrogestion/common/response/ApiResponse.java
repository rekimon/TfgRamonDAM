package com.tfg.agrogestion.common.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
		private final boolean success;
	    private final String mensaje;
	    private final T data;

	    @Builder.Default
	    private final LocalDateTime timestamp = LocalDateTime.now();

	    public static <T> ApiResponse<T> ok(T data) {
	        return ApiResponse.<T>builder()
	                .success(true)
	                .data(data)
	                .build();
	    }

	    public static <T> ApiResponse<T> ok(String mensaje, T data) {
	        return ApiResponse.<T>builder()
	                .success(true)
	                .mensaje(mensaje)
	                .data(data)
	                .build();
	    }

	    public static <T> ApiResponse<T> mensaje(String mensaje) {
	        return ApiResponse.<T>builder()
	                .success(true)
	                .mensaje(mensaje)
	                .build();
	    }
}
