package com.tfg.agrogestion.domain.tarea.service;

import com.tfg.agrogestion.common.enums.RolNombre;
import com.tfg.agrogestion.common.exception.BusinessException;
import com.tfg.agrogestion.common.exception.ResourceNotFoundException;
import com.tfg.agrogestion.common.service.AccesoService;
import com.tfg.agrogestion.domain.cultivo.entity.Cultivo;
import com.tfg.agrogestion.domain.cultivo.repository.CultivoRepository;
import com.tfg.agrogestion.domain.parcela.entity.Parcela;
import com.tfg.agrogestion.domain.parcela.repository.ParcelaRepository;
import com.tfg.agrogestion.domain.tarea.dto.request.ActualizarTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.request.CrearTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.response.TareaResponse;
import com.tfg.agrogestion.domain.tarea.entity.Tarea;
import com.tfg.agrogestion.domain.tarea.mapper.TareaMapper;
import com.tfg.agrogestion.domain.tarea.repository.TareaRepository;
import com.tfg.agrogestion.domain.user.entity.Usuario;
import com.tfg.agrogestion.domain.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final TareaMapper tareaMapper;
    private final ParcelaRepository parcelaRepository;
    private final CultivoRepository cultivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AccesoService accesoService;

    @Override
    @Transactional
    public TareaResponse crear(CrearTareaRequest request,
            String emailUsuario) {
        Parcela parcela = findParcelaOrThrow(request.getParcelaId());
        accesoService.verificarAccesoParcela(parcela, emailUsuario);

        Usuario creadoPor = accesoService.resolverUsuario(emailUsuario);

        Cultivo cultivo = null;
        if (request.getCultivoId() != null) {
            cultivo = cultivoRepository
                    .findByIdAndActivoTrue(request.getCultivoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cultivo", request.getCultivoId()));
        }

        Usuario asignadoA = null;
        if (request.getAsignadoAId() != null) {
            asignadoA = usuarioRepository
                    .findById(request.getAsignadoAId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuario", request.getAsignadoAId()));
        }

        Tarea tarea = Tarea.builder()
                .parcela(parcela)
                .cultivo(cultivo)
                .creadoPor(creadoPor)
                .asignadoA(asignadoA)
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .prioridad(request.getPrioridad() != null
                        ? request.getPrioridad() : "MEDIA")
                .estado("PENDIENTE")
                .fechaPrevista(request.getFechaPrevista())
                .build();

        return tareaMapper.toResponse(tareaRepository.save(tarea));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TareaResponse> listar(Long parcelaId, String estado,
            String prioridad, Long asignadoAId,
            LocalDate desde, LocalDate hasta,
            String emailUsuario, Pageable pageable) {

        Usuario usuario = accesoService.resolverUsuario(emailUsuario);

        // Worker solo ve sus propias tareas
        Long filtroAsignadoA = RolNombre.ROLE_WORKER.equals(usuario.getRol())
                ? usuario.getId()
                : asignadoAId;

        return tareaRepository.buscarConFiltros(
                parcelaId, estado, prioridad,
                filtroAsignadoA, desde, hasta, pageable)
                .map(tareaMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TareaResponse obtenerPorId(Long id, String emailUsuario) {
        Tarea tarea = findTareaOrThrow(id);
        accesoService.verificarAccesoParcela(
                tarea.getParcela(), emailUsuario);
        return tareaMapper.toResponse(tarea);
    }

    @Override
    @Transactional
    public TareaResponse actualizar(Long id,
            ActualizarTareaRequest request, String emailUsuario) {
        Tarea tarea = findTareaOrThrow(id);
        Usuario usuario = accesoService.resolverUsuario(emailUsuario);

        // Worker solo puede actualizar sus propias tareas
        if (RolNombre.ROLE_WORKER.equals(usuario.getRol())) {
            if (tarea.getAsignadoA() == null ||
                    !tarea.getAsignadoA().getId().equals(usuario.getId())) {
                throw new BusinessException(
                        "Solo puedes actualizar tareas asignadas a ti",
                        HttpStatus.FORBIDDEN);
            }
        }

        if (StringUtils.hasText(request.getTitulo()))
            tarea.setTitulo(request.getTitulo());
        if (StringUtils.hasText(request.getDescripcion()))
            tarea.setDescripcion(request.getDescripcion());
        if (StringUtils.hasText(request.getPrioridad()))
            tarea.setPrioridad(request.getPrioridad());
        if (StringUtils.hasText(request.getEstado())) {
            tarea.setEstado(request.getEstado());
            if ("COMPLETADA".equals(request.getEstado()))
                tarea.setFechaCompletada(LocalDateTime.now());
        }
        if (request.getFechaPrevista() != null)
            tarea.setFechaPrevista(request.getFechaPrevista());
        if (request.getAsignadoAId() != null) {
            Usuario asignadoA = usuarioRepository
                    .findById(request.getAsignadoAId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Usuario", request.getAsignadoAId()));
            tarea.setAsignadoA(asignadoA);
        }
        if (StringUtils.hasText(request.getNotasCompletado()))
            tarea.setNotasCompletado(request.getNotasCompletado());

        return tareaMapper.toResponse(tareaRepository.save(tarea));
    }

    @Override
    @Transactional
    public void eliminar(Long id, String emailUsuario) {
        Tarea tarea = findTareaOrThrow(id);
        accesoService.verificarAccesoParcela(
                tarea.getParcela(), emailUsuario);
        tareaRepository.delete(tarea);
    }

    private Tarea findTareaOrThrow(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarea", id));
    }

    private Parcela findParcelaOrThrow(Long id) {
        return parcelaRepository.findByIdAndActivaTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcela", id));
    }
}