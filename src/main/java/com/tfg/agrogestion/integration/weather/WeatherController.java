package com.tfg.agrogestion.integration.weather;

import com.tfg.agrogestion.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Weather", description = "Previsión meteorológica")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener previsión 5 días")
    public ResponseEntity<ApiResponse<WeatherForecastDTO>> forecast(
            @RequestParam double lat,
            @RequestParam double lon) {
        WeatherForecastDTO forecast =
                weatherService.obtenerPrevisión(lat, lon);
        if (forecast == null) {
            return ResponseEntity.ok(
                    ApiResponse.mensaje("No se pudo obtener la previsión"));
        }
        return ResponseEntity.ok(
                ApiResponse.ok("Previsión obtenida", forecast));
    }
}