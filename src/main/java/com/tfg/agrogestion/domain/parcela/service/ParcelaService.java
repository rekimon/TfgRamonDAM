package com.tfg.agrogestion.domain.parcela.service;

import com.tfg.agrogestion.domain.parcela.dto.request.ActualizarParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.request.CrearParcelaRequest;
import com.tfg.agrogestion.domain.parcela.dto.response.ParcelaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParcelaService {

    ParcelaResponse crear(CrearParcelaRequest request, String emailOwner);

    Page<ParcelaResponse> listar(String emailUsuario, String busqueda,
            Pageable pageable);

    ParcelaResponse obtenerPorId(Long id, String emailUsuario);

    ParcelaResponse actualizar(Long id, ActualizarParcelaRequest request,
            String emailUsuario);

    void eliminar(Long id, String emailUsuario);
}