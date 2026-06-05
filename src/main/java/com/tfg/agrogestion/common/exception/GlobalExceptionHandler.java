package com.tfg.agrogestion.common.exception;

import com.tfg.agrogestion.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), req);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex, HttpServletRequest req) {
        return build(ex.getStatus(), "Error de negocio", ex.getMessage(), req);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflicto", ex.getMessage(), req);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuth(
            AuthException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Error de autenticacion", ex.getMessage(), req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas",
                "Email o contrasena incorrectos", req);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(
            DisabledException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Cuenta deshabilitada", ex.getMessage(), req);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLocked(
            LockedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Cuenta bloqueada", ex.getMessage(), req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Acceso denegado",
                "No tiene permisos para esta operacion", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        Map<String, String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null
                                ? fe.getDefaultMessage() : "Valor invalido",
                        (e1, e2) -> e1
                ));

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validacion fallida")
                .mensaje("Corrija los errores en los campos indicados")
                .path(req.getRequestURI())
                .timestamp(LocalDateTime.now())
                .erroresCampos(errores)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest req) {
        log.error("Error no controlado en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor",
                "Ha ocurrido un error inesperado", req);
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status, String error, String mensaje, HttpServletRequest req) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .status(status.value())
                        .error(error)
                        .mensaje(mensaje)
                        .path(req.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}