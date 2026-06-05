package com.tfg.agrogestion.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;

    public BusinessException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public BusinessException(String mensaje) {
        super(mensaje);
        this.status = HttpStatus.BAD_REQUEST;
    }
}