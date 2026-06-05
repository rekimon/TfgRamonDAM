package com.tfg.agrogestion.domain.sensor.service;

import com.tfg.agrogestion.domain.sensor.dto.request.SensorDatosRequest;
import com.tfg.agrogestion.domain.sensor.dto.response.SensorDatosResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface SensorDatosService {

    SensorDatosResponse registrar(SensorDatosRequest request);

    Page<SensorDatosResponse> listarPorParcela(Long parcelaId,
            LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    SensorDatosResponse obtenerUltimoPorParcela(Long parcelaId);
}