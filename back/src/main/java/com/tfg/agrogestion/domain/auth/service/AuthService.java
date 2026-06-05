package com.tfg.agrogestion.domain.auth.service;

import com.tfg.agrogestion.domain.auth.dto.request.LoginRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RefreshTokenRequest;
import com.tfg.agrogestion.domain.auth.dto.request.RegistroRequest;
import com.tfg.agrogestion.domain.auth.dto.response.AuthResponse;

public interface AuthService {
    void registrar(RegistroRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
}