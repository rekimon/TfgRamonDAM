package com.tfg.agrogestion.integration.homeassistant;

import com.tfg.agrogestion.domain.alerta.service.AlertaEngineService;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import com.tfg.agrogestion.domain.sensor.repository.SensorDatosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class HomeAssistantScheduler {

    private final HomeAssistantClient haClient;
    private final HomeAssistantConfig haConfig;
    private final SensorDatosRepository sensorDatosRepository;
    private final ParcelaRepository parcelaRepository;
    private final AlertaEngineService alertaEngineService;

    private final Random random = new Random();

    private static final String SENSOR_TEMPERATURA =
            "sensor.agsex_sdf_huerto_lht65n_temperatura";
    private static final String SENSOR_HUMEDAD_AMB =
            "sensor.agsex_sdf_huerto_lht65n_humedad";
    private static final String SENSOR_LUMINOSIDAD =
            "sensor.agsex_sdf_huerto_lht65n_iluminacionexterno";
    private static final String SENSOR_HUM_TIERRA =
            "sensor.agsex_sdf_invernadero_lht65n_humedad";

    @Scheduled(fixedDelay = 300000, initialDelay = 15000)
    @Transactional
    public void sincronizarSensores() {
        sincronizarSensoresReales();
        generarDatosAleatorios();
    }

    private void sincronizarSensoresReales() {
        log.info("Sincronizando sensores reales de Home Assistant...");

        Optional<Parcela> parcelaOpt = parcelaRepository
                .findByIdAndActivaTrue(haConfig.getParcelaId());

        if (parcelaOpt.isEmpty()) {
            log.warn("Parcela {} no encontrada", haConfig.getParcelaId());
            return;
        }

        Parcela parcela = parcelaOpt.get();

        Double temperatura = getValorSensor(SENSOR_TEMPERATURA);
        Double humedadAmb = getValorSensor(SENSOR_HUMEDAD_AMB);
        Double luminosidad = getValorSensor(SENSOR_LUMINOSIDAD);
        Double humedadTierra = getValorSensor(SENSOR_HUM_TIERRA);

        if (temperatura == null && humedadAmb == null
                && luminosidad == null && humedadTierra == null) {
            log.warn("Sin datos de sensores reales");
            return;
        }

        SensorDatos datos = SensorDatos.builder()
                .parcela(parcela)
                .temperatura(toBigDecimal(temperatura))
                .humedadAmbiental(toBigDecimal(humedadAmb))
                .luminosidad(toBigDecimal(luminosidad))
                .humedadSuelo(toBigDecimal(humedadTierra))
                .timestamp(LocalDateTime.now())
                .build();

        SensorDatos guardado = sensorDatosRepository.save(datos);

        log.info("Sensores reales — Temp: {}°C | Hum.Amb: {}% "
                + "| Lum: {} lx | Hum.Suelo: {}%",
                temperatura, humedadAmb, luminosidad, humedadTierra);

        alertaEngineService.procesarPayload(guardado);
    }

    private void generarDatosAleatorios() {
        List<Parcela> todasParcelas = parcelaRepository
                .findByActivaTrue(PageRequest.of(0, 100))
                .getContent();

        List<Parcela> parcelasSinSensor = todasParcelas.stream()
                .filter(p -> !p.getId().equals(haConfig.getParcelaId()))
                .toList();

        if (parcelasSinSensor.isEmpty()) {
            log.debug("No hay parcelas adicionales para generar datos");
            return;
        }

        log.info("Generando datos aleatorios para {} parcelas...",
                parcelasSinSensor.size());

        for (Parcela parcela : parcelasSinSensor) {
            SensorDatos datos = SensorDatos.builder()
                    .parcela(parcela)
                    .temperatura(randomDecimal(10.0, 35.0))
                    .humedadSuelo(randomDecimal(20.0, 85.0))
                    .humedadAmbiental(randomDecimal(30.0, 90.0))
                    .luminosidad(randomDecimal(5000.0, 80000.0))
                    .timestamp(LocalDateTime.now())
                    .build();

            SensorDatos guardado = sensorDatosRepository.save(datos);

            log.info("Parcela '{}' — Temp: {}°C | Hum.Suelo: {}% "
                    + "| Hum.Amb: {}% | Lum: {} lx",
                    parcela.getNombre(),
                    datos.getTemperatura(),
                    datos.getHumedadSuelo(),
                    datos.getHumedadAmbiental(),
                    datos.getLuminosidad());

            alertaEngineService.procesarPayload(guardado);
        }
    }

    private BigDecimal randomDecimal(double min, double max) {
        double valor = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private Double getValorSensor(String entityId) {
        HomeAssistantSensorDTO dto = haClient.getSensorState(entityId);
        if (dto == null) return null;
        return dto.getStateAsDouble();
    }

    private BigDecimal toBigDecimal(Double valor) {
        if (valor == null) return null;
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }
}