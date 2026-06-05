package com.tfg.agrogestion.common.exception;

public class AuthException extends RuntimeException {
    public AuthException(String mensaje) {
        super(mensaje);
    }
}