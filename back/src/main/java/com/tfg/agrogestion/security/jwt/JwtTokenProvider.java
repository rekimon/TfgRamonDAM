package com.tfg.agrogestion.security.jwt;

import com.tfg.agrogestion.security.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            CustomUserDetailsService userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.userDetailsService = userDetailsService;
    }

    public String generarAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generarTokenDesdeEmail(userDetails.getUsername());
    }

    public String generarTokenDesdeEmail(String email) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(secretKey)
                .compact();
    }

    public String extraerEmail(String token) {
        return parsearClaims(token).getSubject();
    }

    public boolean validarToken(String token) {
        try {
            parsearClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT no soportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT malformado: {}", e.getMessage());
        } catch (SecurityException e) {
            log.warn("Firma JWT invalida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT vacio o nulo: {}", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        String email = extraerEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}