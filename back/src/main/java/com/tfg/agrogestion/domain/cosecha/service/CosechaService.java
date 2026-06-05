package com.tfg.agrogestion.domain.cosecha.service;

import com.tfg.agrogestion.domain.cosecha.dto.request.ActualizarCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.request.CrearCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.response.CosechaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CosechaService {

    CosechaResponse crear(CrearCosechaRequest request, String emailUsuario);

    Page<CosechaResponse> listarPorCultivo(Long cultivoId,
            String emailUsuario, Pageable pageable);

    Page<CosechaResponse> listarPorParcela(Long parcelaId,
            LocalDate desde, LocalDate hasta,
            String emailUsuario, Pageable pageable);

    CosechaResponse obtenerPorId(Long id, String emailUsuario);

    CosechaResponse actualizar(Long id, ActualizarCosechaRequest request,
            String emailUsuario);

    void eliminar(Long id, String emailUsuario);
}