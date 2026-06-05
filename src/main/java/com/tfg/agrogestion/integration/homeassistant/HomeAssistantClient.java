package com.tfg.agrogestion.integration.homeassistant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class HomeAssistantClient {

    private final HomeAssistantConfig config;

    public HomeAssistantSensorDTO getSensorState(String entityId) {
        try {
            WebClient client = WebClient.builder()
                    .baseUrl(config.getUrl())
                    .defaultHeader("Authorization",
                            "Bearer " + config.getToken())
                    .defaultHeader("Accept", "application/json")
                    .build();

            return client.get()
                    .uri("/api/states/" + entityId)
                    .retrieve()
                    .bodyToMono(HomeAssistantSensorDTO.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
        } catch (Exception e) {
            log.warn("Error consultando sensor {}: {}", entityId,
                    e.getMessage());
            return null;
        }
    }
}