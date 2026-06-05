package com.tfg.agrogestion.integration.weather;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WeatherForecastDTO {
    private String ciudad;
    private List<DiaPrevisión> dias;

    @Getter
    @Builder
    public static class DiaPrevisión {
        private String fecha;
        private String diaSemana;
        private double tempMax;
        private double tempMin;
        private double tempMedia;
        private int humedad;
        private double probabilidadLluvia;
        private String descripcion;
        private String icono;
        private double viento;
    }
}