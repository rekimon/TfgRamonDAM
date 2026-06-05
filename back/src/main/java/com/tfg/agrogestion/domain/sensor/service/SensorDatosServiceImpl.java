package com.tfg.agrogestion.domain.sensor.service;

import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.domain.alerta.service.AlertaEngineService;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.sensor.dto.request.SensorDatosRequest;
import com.tfg.agrogestion.domain.sensor.dto.response.SensorDatosResponse;
import com.tfg.agrogestion.domain.sensor.entity.SensorDatos;
import com.tfg.agrogestion.domain.sensor.mapper.SensorDatosMapper;
import com.tfg.agrogestion.domain.sensor.repository.SensorDatosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorDatosServiceImpl implements SensorDatosService {

    private final SensorDatosRepository sensorDatosRepository;
    private final SensorDatosMapper sensorDatosMapper;
    private final ParcelaRepository parcelaRepository;
    private final AlertaEngineService alertaEngineService;

    @Override
    @Transactional
    public SensorDatosResponse registrar(SensorDatosRequest request) {
        Parcela parcela = parcelaRepository
                .findByIdAndActivaTrue(request.getParcelaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcela", request.getParcelaId()));

        SensorDatos datos = SensorDatos.builder()
                .parcela(parcela)
                .temperatura(request.getTemperatura())
                .humedadSuelo(request.getHumedadSuelo())
                .humedadAmbiental(request.getHumedadAmbiental())
                .luminosidad(request.getLuminosidad())
                .timestamp(request.getTimestamp())
                .build();

        SensorDatos guardado = sensorDatosRepository.save(datos);

        // Disparar motor de alertas de forma asincrona
        alertaEngineService.procesarPayload(guardado);

        return sensorDatosMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorDatosResponse> listarPorParcela(Long parcelaId,
            LocalDateTime desde, LocalDateTime hasta, Pageable pageable) {
        if (desde != null && hasta != null) {
            return sensorDatosRepository
                    .findByParcelaAndRangoFecha(parcelaId, desde, hasta, pageable)
                    .map(sensorDatosMapper::toResponse);
        }
        return sensorDatosRepository
                .findByParcelaIdOrderByTimestampDesc(parcelaId, pageable)
                .map(sensorDatosMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SensorDatosResponse obtenerUltimoPorParcela(Long parcelaId) {
        return sensorDatosRepository
                .findTopByParcelaIdOrderByTimestampDesc(parcelaId)
                .map(sensorDatosMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay datos de sensores para la parcela: "
                        + parcelaId));
    }
}