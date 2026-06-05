package com.tfg.agrogestion.domain.tipocultivo.service;

import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoCultivoService {

    Page<TipoCultivoResumenResponse> listar(String busqueda, Pageable pageable);

    TipoCultivoResponse obtenerPorId(Long id);
}