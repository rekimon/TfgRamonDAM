package com.tfg.agrogestion.domain.alerta.repository;

import com.tfg.agrogestion.domain.alerta.entity.ReglaAlertaManual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReglaAlertaManualRepository
        extends JpaRepository<ReglaAlertaManual, Long> {

    Page<ReglaAlertaManual> findByParcelaId(Long parcelaId, Pageable pageable);

    List<ReglaAlertaManual> findByParcelaIdAndActivaTrue(Long parcelaId);
}