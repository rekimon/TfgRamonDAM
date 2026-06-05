package com.tfg.agrogestion.domain.cosecha.service;

import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.common.service.AccesoService;
import com.tfg.agrogestion.domain.cosecha.dto.request.ActualizarCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.request.CrearCosechaRequest;
import com.tfg.agrogestion.domain.cosecha.dto.response.CosechaResponse;
import com.tfg.agrogestion.domain.cosecha.entity.Cosecha;
import com.tfg.agrogestion.domain.cosecha.mapper.CosechaMapper;
import com.tfg.agrogestion.domain.cosecha.repository.CosechaRepository;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.cultivo.repository.CultivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CosechaServiceImpl implements CosechaService {

    private final CosechaRepository cosechaRepository;
    private final CosechaMapper cosechaMapper;
    private final CultivoRepository cultivoRepository;
    private final AccesoService accesoService;

    @Override
    @Transactional
    public CosechaResponse crear(CrearCosechaRequest request,
            String emailUsuario) {
        Cultivo cultivo = findCultivoOrThrow(request.getCultivoId());
        accesoService.verificarAccesoParcela(
                cultivo.getParcela(), emailUsuario);

        Cosecha cosecha = Cosecha.builder()
                .cultivo(cultivo)
                .fechaCosecha(request.getFechaCosecha())
                .kgObtenidos(request.getKgObtenidos())
                .precioPorKg(request.getPrecioPorKg())
                .calidad(request.getCalidad() != null
                        ? request.getCalidad() : "ESTANDAR")
                .observaciones(request.getObservaciones())
                .build();

        return cosechaMapper.toResponse(cosechaRepository.save(cosecha));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CosechaResponse> listarPorCultivo(Long cultivoId,
            String emailUsuario, Pageable pageable) {
        Cultivo cultivo = findCultivoOrThrow(cultivoId);
        accesoService.verificarAccesoParcela(
                cultivo.getParcela(), emailUsuario);
        return cosechaRepository
                .findByCultivoId(cultivoId, pageable)
                .map(cosechaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CosechaResponse> listarPorParcela(Long parcelaId,
            LocalDate desde, LocalDate hasta,
            String emailUsuario, Pageable pageable) {
        return cosechaRepository
                .findByParcelaConFiltros(parcelaId, desde, hasta, pageable)
                .map(cosechaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CosechaResponse obtenerPorId(Long id, String emailUsuario) {
        Cosecha cosecha = findCosechaOrThrow(id);
        accesoService.verificarAccesoParcela(
                cosecha.getCultivo().getParcela(), emailUsuario);
        return cosechaMapper.toResponse(cosecha);
    }

    @Override
    @Transactional
    public CosechaResponse actualizar(Long id,
            ActualizarCosechaRequest request, String emailUsuario) {
        Cosecha cosecha = findCosechaOrThrow(id);
        accesoService.verificarAccesoParcela(
                cosecha.getCultivo().getParcela(), emailUsuario);

        if (request.getFechaCosecha() != null)
            cosecha.setFechaCosecha(request.getFechaCosecha());
        if (request.getKgObtenidos() != null)
            cosecha.setKgObtenidos(request.getKgObtenidos());
        if (request.getPrecioPorKg() != null)
            cosecha.setPrecioPorKg(request.getPrecioPorKg());
        if (request.getCalidad() != null)
            cosecha.setCalidad(request.getCalidad());
        if (request.getObservaciones() != null)
            cosecha.setObservaciones(request.getObservaciones());

        return cosechaMapper.toResponse(cosechaRepository.save(cosecha));
    }

    @Override
    @Transactional
    public void eliminar(Long id, String emailUsuario) {
        Cosecha cosecha = findCosechaOrThrow(id);
        accesoService.verificarAccesoParcela(
                cosecha.getCultivo().getParcela(), emailUsuario);
        cosechaRepository.delete(cosecha);
    }

    private Cultivo findCultivoOrThrow(Long id) {
        return cultivoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cultivo", id));
    }

    private Cosecha findCosechaOrThrow(Long id) {
        return cosechaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cosecha", id));
    }
}