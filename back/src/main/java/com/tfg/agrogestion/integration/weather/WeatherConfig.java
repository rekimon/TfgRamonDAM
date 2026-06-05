package com.tfg.agrogestion.integration.weather;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openweathermap")
@Getter
@Setter
public class WeatherConfig {
    private String apiKey;
    private String url;
    private String unidades = "metric";
}