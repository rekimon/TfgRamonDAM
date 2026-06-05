package com.tfg.agrogestion.domain.cultivo.service;

import com.tfg.agrogestion.domain.cultivo.dto.request.ActualizarCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.request.CrearCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CultivoService {

    CultivoResponse crear(CrearCultivoRequest request, String emailUsuario);

    Page<CultivoResponse> listarPorParcela(Long parcelaId,
            String estado, String busqueda,
            String emailUsuario, Pageable pageable);

    CultivoResponse obtenerPorId(Long id, String emailUsuario);

    CultivoResponse actualizar(Long id, ActualizarCultivoRequest request,
            String emailUsuario);

    void eliminar(Long id, String emailUsuario);
}