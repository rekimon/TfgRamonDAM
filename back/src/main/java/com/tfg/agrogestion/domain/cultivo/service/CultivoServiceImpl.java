package com.tfg.agrogestion.domain.cultivo.service;

import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.common.service.AccesoService;
import com.tfg.agrogestion.domain.cultivo.dto.request.ActualizarCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.request.CrearCultivoRequest;
import com.tfg.agrogestion.domain.cultivo.dto.response.CultivoResponse;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.cultivo.mapper.CultivoMapper;
import com.tfg.agrogestion.domain.cultivo.repository.CultivoRepository;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.tipocultivo.entity.TipoCultivo;
import com.tfg.agrogestion.domain.tipocultivo.repository.TipoCultivoRepository;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CultivoServiceImpl implements CultivoService {

    private final CultivoRepository cultivoRepository;
    private final CultivoMapper cultivoMapper;
    private final ParcelaRepository parcelaRepository;
    private final TipoCultivoRepository tipoCultivoRepository;
    private final AccesoService accesoService;

    @Override
    @Transactional
    public CultivoResponse crear(CrearCultivoRequest request,
            String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(request.getParcelaId());
        accesoService.verificarAccesoParcela(parcela, emailUsuario);

        TipoCultivo tipoCultivo = tipoCultivoRepository
                .findById(request.getTipoCultivoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TipoCultivo", request.getTipoCultivoId()));

        Cultivo cultivo = Cultivo.builder()
                .parcela(parcela)
                .tipoCultivo(tipoCultivo)
                .nombrePersonalizado(request.getNombrePersonalizado())
                .fechaSiembra(request.getFechaSiembra())
                .fechaCosechaEstimada(request.getFechaCosechaEstimada())
                .estado("ACTIVO")
                .notas(request.getNotas())
                .activo(true)
                .build();

        return cultivoMapper.toResponse(cultivoRepository.save(cultivo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CultivoResponse> listarPorParcela(Long parcelaId,
            String estado, String busqueda,
            String emailUsuario, Pageable pageable) {
        Parcela parcela = findParcelaOrThrow(parcelaId);
        accesoService.verificarAccesoParcela(parcela, emailUsuario);
        return cultivoRepository
                .buscarConFiltros(parcelaId, estado, busqueda, pageable)
                .map(cultivoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CultivoResponse obtenerPorId(Long id, String emailUsuario) {
        Cultivo cultivo = findCultivoOrThrow(id);
        accesoService.verificarAccesoParcela(
                cultivo.getParcela(), emailUsuario);
        return cultivoMapper.toResponse(cultivo);
    }

    @Override
    @Transactional
    public CultivoResponse actualizar(Long id,
            ActualizarCultivoRequest request, String emailUsuario) {
        Cultivo cultivo = findCultivoOrThrow(id);
        Usuario usuario = accesoService.resolverUsuario(emailUsuario);

        // Worker solo puede cambiar estado
        if (RolNombre.ROLE_WORKER.equals(usuario.getRol())) {
            if (request.getEstado() != null)
                cultivo.setEstado(request.getEstado());
            return cultivoMapper.toResponse(cultivoRepository.save(cultivo));
        }

        // Owner y Admin pueden cambiar todo
        if (request.getNombrePersonalizado() != null)
            cultivo.setNombrePersonalizado(request.getNombrePersonalizado());
        if (request.getFechaCosechaEstimada() != null)
            cultivo.setFechaCosechaEstimada(request.getFechaCosechaEstimada());
        if (request.getEstado() != null)
            cultivo.setEstado(request.getEstado());
        if (request.getNotas() != null)
            cultivo.setNotas(request.getNotas());

        return cultivoMapper.toResponse(cultivoRepository.save(cultivo));
    }

    @Override
    @Transactional
    public void eliminar(Long id, String emailUsuario) {
        Cultivo cultivo = findCultivoOrThrow(id);
        accesoService.verificarAccesoParcela(
                cultivo.getParcela(), emailUsuario);
        cultivo.setActivo(false);
        cultivoRepository.save(cultivo);
    }

    private Parcela findParcelaOrThrow(Long id) {
        return parcelaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcela", id));
    }

    private Cultivo findCultivoOrThrow(Long id) {
        return cultivoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cultivo", id));
    }
}