package com.tfg.agrogestion.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.agrogestion.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("No autenticado")
                .mensaje("Debe autenticarse para acceder a este recurso")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        objectMapper.writeValue(response.getOutputStream(), error);
    }
}