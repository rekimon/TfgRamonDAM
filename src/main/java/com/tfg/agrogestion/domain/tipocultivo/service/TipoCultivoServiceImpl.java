package com.tfg.agrogestion.domain.tipocultivo.service;

import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResumenResponse;
import com.tfg.agrogestion.domain.tipocultivo.dto.response.TipoCultivoResponse;
import com.tfg.agrogestion.domain.tipocultivo.mapper.TipoCultivoMapper;
import com.tfg.agrogestion.domain.tipocultivo.repository.TipoCultivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TipoCultivoServiceImpl implements TipoCultivoService {

    private final TipoCultivoRepository tipoCultivoRepository;
    private final TipoCultivoMapper tipoCultivoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TipoCultivoResumenResponse> listar(
            String busqueda, Pageable pageable) {
        return tipoCultivoRepository
                .buscarConFiltros(busqueda, pageable)
                .map(tipoCultivoMapper::toResumen);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCultivoResponse obtenerPorId(Long id) {
        return tipoCultivoMapper.toResponse(
                tipoCultivoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "TipoCultivo", id)));
    }
}