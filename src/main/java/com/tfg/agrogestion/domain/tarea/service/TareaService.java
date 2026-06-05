package com.tfg.agrogestion.domain.tarea.service;

import com.tfg.agrogestion.domain.tarea.dto.request.ActualizarTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.request.CrearTareaRequest;
import com.tfg.agrogestion.domain.tarea.dto.response.TareaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TareaService {

    TareaResponse crear(CrearTareaRequest request, String emailUsuario);

    Page<TareaResponse> listar(Long parcelaId, String estado,
            String prioridad, Long asignadoAId,
            LocalDate desde, LocalDate hasta,
            String emailUsuario, Pageable pageable);

    TareaResponse obtenerPorId(Long id, String emailUsuario);

    TareaResponse actualizar(Long id, ActualizarTareaRequest request,
            String emailUsuario);

    void eliminar(Long id, String emailUsuario);
}