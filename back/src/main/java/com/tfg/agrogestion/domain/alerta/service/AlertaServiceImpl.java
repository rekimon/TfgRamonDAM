package com.tfg.agrogestion.domain.alerta.service;

import com.tfg.agrogestion.common.exception.BusinessException;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.common.service.AccesoService;
import com.tfg.agrogestion.domain.alerta.dto.request.ActualizarReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.request.CrearReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.response.AlertaResponse;
import com.tfg.agrogestion.domain.alerta.dto.response.ReglaAlertaResponse;
import com.tfg.agrogestion.domain.alerta.entity.Alerta;
import com.tfg.agrogestion.domain.alerta.entity.ReglaAlertaManual;
import com.tfg.agrogestion.domain.alerta.mapper.AlertaMapper;
import com.tfg.agrogestion.domain.alerta.mapper.ReglaAlertaMapper;
import com.tfg.agrogestion.domain.alerta.repository.AlertaRepository;
import com.tfg.agrogestion.domain.alerta.repository.ReglaAlertaManualRepository;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final ReglaAlertaManualRepository reglaRepository;
    private final AlertaMapper alertaMapper;
    private final ReglaAlertaMapper reglaMapper;
    private final ParcelaRepository parcelaRepository;
    private final AccesoService accesoService;

    @Override
    @Transactional
    public ReglaAlertaResponse crearRegla(CrearReglaAlertaRequest request,
            String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(request.getParcelaId());
        Usuario usuario = accesoService.resolverUsuario(emailUsuario);
        accesoService.verificarAccesoParcela(parcela, emailUsuario);

        ReglaAlertaManual regla = ReglaAlertaManual.builder()
                .parcela(parcela)
                .creadoPor(usuario)
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .campo(request.getCampo())
                .operador(request.getOperador())
                .valorUmbral(request.getValorUmbral())
                .valorUmbralMax(request.getValorUmbralMax())
                .severidad(request.getSeveridad() != null
                        ? request.getSeveridad() : "MEDIA")
                .activa(true)
                .build();

        return reglaMapper.toResponse(reglaRepository.save(regla));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReglaAlertaResponse> listarReglasPorParcela(Long parcelaId,
            String emailUsuario, Pageable pageable) {
        Parcela parcela = findParcelaOrThrow(parcelaId);
        accesoService.verificarAccesoParcela(parcela, emailUsuario);
        return reglaRepository.findByParcelaId(parcelaId, pageable)
                .map(reglaMapper::toResponse);
    }

    @Override
    @Transactional
    public ReglaAlertaResponse actualizarRegla(Long id,
            ActualizarReglaAlertaRequest request, String emailUsuario) {
        ReglaAlertaManual regla = reglaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ReglaAlerta", id));
        accesoService.verificarAccesoParcela(
                regla.getParcela(), emailUsuario);

        if (StringUtils.hasText(request.getNombre()))
            regla.setNombre(request.getNombre());
        if (StringUtils.hasText(request.getDescripcion()))
            regla.setDescripcion(request.getDescripcion());
        if (request.getValorUmbral() != null)
            regla.setValorUmbral(request.getValorUmbral());
        if (request.getValorUmbralMax() != null)
            regla.setValorUmbralMax(request.getValorUmbralMax());
        if (StringUtils.hasText(request.getSeveridad()))
            regla.setSeveridad(request.getSeveridad());
        if (request.getActiva() != null)
            regla.setActiva(request.getActiva());

        return reglaMapper.toResponse(reglaRepository.save(regla));
    }

    @Override
    @Transactional
    public void eliminarRegla(Long id, String emailUsuario) {
        ReglaAlertaManual regla = reglaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "ReglaAlerta", id));
        accesoService.verificarAccesoParcela(
                regla.getParcela(), emailUsuario);
        reglaRepository.delete(regla);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertaResponse> listarAlertasPorParcela(Long parcelaId,
            String estado, String severidad,
            String emailUsuario, Pageable pageable) {
        Parcela parcela = findParcelaOrThrow(parcelaId);
        accesoService.verificarAccesoParcela(parcela, emailUsuario);
        return alertaRepository.buscarConFiltros(
                parcelaId, estado, severidad, pageable)
                .map(alertaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertaResponse> listarTodas(String estado,
            String severidad, Pageable pageable) {
        return alertaRepository.buscarTodas(estado, severidad, pageable)
                .map(alertaMapper::toResponse);
    }

    @Override
    @Transactional
    public AlertaResponse reconocerAlerta(Long id, String emailUsuario) {
        Alerta alerta = findAlertaOrThrow(id);
        accesoService.verificarAccesoParcela(
                alerta.getParcela(), emailUsuario);

        if (!"ACTIVA".equals(alerta.getEstado())) {
            throw new BusinessException(
                    "Solo se pueden reconocer alertas en estado ACTIVA");
        }

        Usuario usuario = accesoService.resolverUsuario(emailUsuario);
        alerta.setEstado("RECONOCIDA");
        alerta.setReconocidaPor(usuario);
        alerta.setReconocidaEn(LocalDateTime.now());

        return alertaMapper.toResponse(alertaRepository.save(alerta));
    }

    @Override
    @Transactional
    public AlertaResponse resolverAlerta(Long id, String emailUsuario) {
        Alerta alerta = findAlertaOrThrow(id);
        accesoService.verificarAccesoParcela(
                alerta.getParcela(), emailUsuario);

        if ("RESUELTA".equals(alerta.getEstado())) {
            throw new BusinessException("La alerta ya esta resuelta");
        }

        alerta.setEstado("RESUELTA");
        alerta.setResueltaEn(LocalDateTime.now());

        return alertaMapper.toResponse(alertaRepository.save(alerta));
    }

    private Parcela findParcelaOrThrow(Long id) {
        return parcelaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcela", id));
    }

    private Alerta findAlertaOrThrow(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alerta", id));
    }
}