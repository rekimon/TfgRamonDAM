package com.tfg.agrogestion.domain.alerta.service;

import com.tfg.agrogestion.domain.alerta.entity.Alerta;
import com.tfg.agrogestion.domain.alerta.entity.ReglaAlertaManual;
import com.tfg.agrogestion.domain.alerta.repository.AlertaRepository;
import com.tfg.agrogestion.domain.alerta.repository.ReglaAlertaManualRepository;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.cultivo.repository.CultivoRepository;
import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaEngineService {

    private final AlertaRepository alertaRepository;
    private final ReglaAlertaManualRepository reglaRepository;
    private final CultivoRepository cultivoRepository;

    @Async
    @Transactional
    public void procesarPayload(SensorDatos datos) {
        try {
            evaluarAlertasAutomaticas(datos);
            evaluarReglasManules(datos);
        } catch (Exception e) {
            log.error("Error en motor de alertas para parcela {}: {}",
                    datos.getParcela().getId(), e.getMessage());
        }
    }

    private void evaluarAlertasAutomaticas(SensorDatos datos) {
        List<Cultivo> cultivos = cultivoRepository
                .findCultivosActivosByParcela(datos.getParcela().getId());

        for (Cultivo cultivo : cultivos) {
            TipoCultivo tc = cultivo.getTipoCultivo();

            // Temperatura
            if (datos.getTemperatura() != null) {
                evaluarTemperatura(datos, cultivo, tc);
            }

            // Humedad suelo
            if (datos.getHumedadSuelo() != null) {
                evaluarHumedadSuelo(datos, cultivo, tc);
            }
        }
    }

    private void evaluarTemperatura(SensorDatos datos,
            Cultivo cultivo, TipoCultivo tc) {
        BigDecimal temp = datos.getTemperatura();

        if (tc.getTempCriticaMin() != null
                && temp.compareTo(tc.getTempCriticaMin()) < 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "HELADA", "CRITICA",
                    "Riesgo de helada. Temperatura: " + temp + "C. "
                    + tc.getRecomendacionHelada(),
                    temp);
        } else if (tc.getTempCriticaMax() != null
                && temp.compareTo(tc.getTempCriticaMax()) > 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "ESTRES_TERMICO", "ALTA",
                    "Estres termico. Temperatura: " + temp + "C",
                    temp);
        } else if (tc.getTempOptimaMin() != null
                && temp.compareTo(tc.getTempOptimaMin()) < 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "TEMPERATURA_BAJA", "MEDIA",
                    "Temperatura por debajo del optimo: " + temp + "C",
                    temp);
        } else if (tc.getTempOptimaMax() != null
                && temp.compareTo(tc.getTempOptimaMax()) > 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "TEMPERATURA_ALTA", "MEDIA",
                    "Temperatura por encima del optimo: " + temp + "C",
                    temp);
        }
    }

    private void evaluarHumedadSuelo(SensorDatos datos,
            Cultivo cultivo, TipoCultivo tc) {
        BigDecimal hum = datos.getHumedadSuelo();

        if (tc.getHumedadSueloCriticaMin() != null
                && hum.compareTo(tc.getHumedadSueloCriticaMin()) < 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "ESTRES_HIDRICO", "BAJA",
                    "Estres hidrico critico. Humedad suelo: " + hum + "%. "
                    + tc.getRecomendacionEstresHidrico(),
                    hum);
        } else if (tc.getHumedadSueloCriticaMax() != null
                && hum.compareTo(tc.getHumedadSueloCriticaMax()) > 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "ENCHARCAMIENTO", "ALTA",
                    "Riesgo de encharcamiento. Humedad suelo: " + hum + "%",
                    hum);
        } else if (tc.getHumedadSueloOptimaMin() != null
                && hum.compareTo(tc.getHumedadSueloOptimaMin()) < 0) {
            generarAlertaAutomatica(datos, cultivo,
                    "HUMEDAD_BAJA", "MEDIA",
                    "Humedad de suelo por debajo del optimo: " + hum + "%",
                    hum);
        }
    }

    private void evaluarReglasManules(SensorDatos datos) {
        List<ReglaAlertaManual> reglas = reglaRepository
                .findByParcelaIdAndActivaTrue(datos.getParcela().getId());

        for (ReglaAlertaManual regla : reglas) {
            BigDecimal valor = obtenerValorCampo(datos, regla.getCampo());
            if (valor == null) continue;

            boolean disparar = evaluarCondicion(
                    valor, regla.getOperador(),
                    regla.getValorUmbral(), regla.getValorUmbralMax());

            if (disparar) {
                List<Alerta> existentes = alertaRepository
                        .findAlertasActivasByTipo(
                                datos.getParcela().getId(),
                                "PERSONALIZADA");
                boolean yaExiste = existentes.stream()
                        .anyMatch(a -> a.getReglaManual() != null
                                && a.getReglaManual().getId()
                                        .equals(regla.getId()));

                if (!yaExiste) {
                    Alerta alerta = Alerta.builder()
                            .parcela(datos.getParcela())
                            .reglaManual(regla)
                            .tipoOrigen("MANUAL")
                            .tipoAlerta("PERSONALIZADA")
                            .severidad(regla.getSeveridad())
                            .mensaje("Regla '" + regla.getNombre()
                                    + "' disparada. Valor: " + valor)
                            .valorDetectado(valor)
                            .fechaDisparo(LocalDateTime.now())
                            .estado("ACTIVA")
                            .build();
                    alertaRepository.save(alerta);
                }
            }
        }
    }

    private void generarAlertaAutomatica(SensorDatos datos,
            Cultivo cultivo, String tipoAlerta, String severidad,
            String mensaje, BigDecimal valorDetectado) {

        List<Alerta> existentes = alertaRepository
                .findAlertasActivasByTipo(
                        datos.getParcela().getId(), tipoAlerta);

        if (!existentes.isEmpty()) return;

        Alerta alerta = Alerta.builder()
                .parcela(datos.getParcela())
                .cultivo(cultivo)
                .tipoOrigen("AUTOMATICA")
                .tipoAlerta(tipoAlerta)
                .severidad(severidad)
                .mensaje(mensaje)
                .valorDetectado(valorDetectado)
                .fechaDisparo(LocalDateTime.now())
                .estado("ACTIVA")
                .build();

        alertaRepository.save(alerta);
        log.info("Alerta automatica generada: {} para parcela {}",
                tipoAlerta, datos.getParcela().getId());
    }

    private BigDecimal obtenerValorCampo(SensorDatos datos, String campo) {
        return switch (campo) {
            case "TEMPERATURA" -> datos.getTemperatura();
            case "HUMEDAD_SUELO" -> datos.getHumedadSuelo();
            case "HUMEDAD_AMBIENTAL" -> datos.getHumedadAmbiental();
            case "LUMINOSIDAD" -> datos.getLuminosidad();
            default -> null;
        };
    }

    private boolean evaluarCondicion(BigDecimal valor, String operador,
            BigDecimal umbral, BigDecimal umbralMax) {
        return switch (operador) {
            case "MAYOR_QUE" -> valor.compareTo(umbral) > 0;
            case "MENOR_QUE" -> valor.compareTo(umbral) < 0;
            case "ENTRE" -> umbralMax != null
                    && valor.compareTo(umbral) >= 0
                    && valor.compareTo(umbralMax) <= 0;
            default -> false;
        };
    }
}