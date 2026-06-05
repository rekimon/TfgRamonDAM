package com.tfg.agrogestion.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.agrogestion.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acceso denegado")
                .mensaje("No tiene permisos para acceder a este recurso")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        objectMapper.writeValue(response.getOutputStream(), error);
    }
}