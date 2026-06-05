package com.tfg.agrogestion.integration.homeassistant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "homeassistant")
@Getter
@Setter
public class HomeAssistantConfig {
    private String url;
    private String token;
    private Long parcelaId;
    private int intervaloMinutos = 5;
}