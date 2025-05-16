package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.AtencionRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;

    @Transactional
    public Atencion registrar(AtencionRegisterDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Atencion atencion = new Atencion();
        atencion.setCita(cita);
        atencion.setFecha(LocalDate.now());
        atencion.setMotivo(dto.getMotivo());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setObservaciones(dto.getObservaciones());

        return atencionRepository.save(atencion);
    }
}
