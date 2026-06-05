package com.tfg.agrogestion.common.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String mensaje) {
        super(mensaje);
    }
}