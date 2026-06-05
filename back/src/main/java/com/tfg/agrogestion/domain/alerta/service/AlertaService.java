package com.tfg.agrogestion.domain.alerta.service;

import com.tfg.agrogestion.domain.alerta.dto.request.ActualizarReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.request.CrearReglaAlertaRequest;
import com.tfg.agrogestion.domain.alerta.dto.response.AlertaResponse;
import com.tfg.agrogestion.domain.alerta.dto.response.ReglaAlertaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertaService {

    ReglaAlertaResponse crearRegla(CrearReglaAlertaRequest request,
            String emailUsuario);

    Page<ReglaAlertaResponse> listarReglasPorParcela(Long parcelaId,
            String emailUsuario, Pageable pageable);

    ReglaAlertaResponse actualizarRegla(Long id,
            ActualizarReglaAlertaRequest request, String emailUsuario);

    void eliminarRegla(Long id, String emailUsuario);

    Page<AlertaResponse> listarAlertasPorParcela(Long parcelaId,
            String estado, String severidad,
            String emailUsuario, Pageable pageable);

    AlertaResponse reconocerAlerta(Long id, String emailUsuario);
    Page<AlertaResponse> listarTodas(String estado, String severidad, Pageable pageable);
    AlertaResponse resolverAlerta(Long id, String emailUsuario);
}