package com.tfg.agrogestion.integration.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherForecastDTO obtenerPrevisión(
            double lat, double lon) {
        try {
            WebClient client = WebClient.builder()
                    .baseUrl(config.getUrl())
                    .build();

            String response = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/forecast")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", config.getApiKey())
                            .queryParam("units", config.getUnidades())
                            .queryParam("lang", "es")
                            .queryParam("cnt", 40)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parsearRespuesta(response);

        } catch (Exception e) {
            log.error("Error obteniendo previsión meteorológica: {}",
                    e.getMessage());
            return null;
        }
    }

    private WeatherForecastDTO parsearRespuesta(String json)
            throws Exception {
        JsonNode root = objectMapper.readTree(json);
        JsonNode lista = root.get("list");
        String ciudad = root.path("city").path("name").asText();

        Map<String, List<JsonNode>> porDia = new LinkedHashMap<>();

        for (JsonNode item : lista) {
            String fecha = item.get("dt_txt").asText().substring(0, 10);
            porDia.computeIfAbsent(fecha, k -> new ArrayList<>()).add(item);
        }

        List<WeatherForecastDTO.DiaPrevisión> dias = new ArrayList<>();
        int count = 0;

        for (Map.Entry<String, List<JsonNode>> entry :
                porDia.entrySet()) {
            if (count >= 5) break;

            String fecha = entry.getKey();
            List<JsonNode> items = entry.getValue();

            double tempMax = items.stream()
                    .mapToDouble(i -> i.path("main")
                            .path("temp_max").asDouble())
                    .max().orElse(0);

            double tempMin = items.stream()
                    .mapToDouble(i -> i.path("main")
                            .path("temp_min").asDouble())
                    .min().orElse(0);

            double tempMedia = items.stream()
                    .mapToDouble(i -> i.path("main")
                            .path("temp").asDouble())
                    .average().orElse(0);

            int humedad = (int) items.stream()
                    .mapToInt(i -> i.path("main")
                            .path("humidity").asInt())
                    .average().orElse(0);

            double probLluvia = items.stream()
                    .mapToDouble(i -> i.path("pop").asDouble())
                    .max().orElse(0) * 100;

            double viento = items.stream()
                    .mapToDouble(i -> i.path("wind")
                            .path("speed").asDouble())
                    .average().orElse(0);

            JsonNode mediodia = items.stream()
                    .filter(i -> i.get("dt_txt").asText()
                            .contains("12:00:00"))
                    .findFirst()
                    .orElse(items.get(items.size() / 2));

            String descripcion = mediodia.path("weather")
                    .get(0).path("description").asText();
            String icono = mediodia.path("weather")
                    .get(0).path("icon").asText();

            LocalDate localDate = LocalDate.parse(fecha);
            String diaSemana = localDate.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL,
                            new Locale("es", "ES"));

            dias.add(WeatherForecastDTO.DiaPrevisión.builder()
                    .fecha(fecha)
                    .diaSemana(capitalize(diaSemana))
                    .tempMax(Math.round(tempMax * 10.0) / 10.0)
                    .tempMin(Math.round(tempMin * 10.0) / 10.0)
                    .tempMedia(Math.round(tempMedia * 10.0) / 10.0)
                    .humedad(humedad)
                    .probabilidadLluvia(
                            Math.round(probLluvia * 10.0) / 10.0)
                    .descripcion(descripcion)
                    .icono(icono)
                    .viento(Math.round(viento * 10.0) / 10.0)
                    .build());

            count++;
        }

        return WeatherForecastDTO.builder()
                .ciudad(ciudad)
                .dias(dias)
                .build();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}