package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionDetailDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Patologia;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.AtencionRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CitaRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.PatologiaRepository; // <-- importar
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;
    private final PatologiaRepository patologiaRepository; // <-- inyectado

    @Transactional
    public Atencion registrar(AtencionRegisterDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        Atencion atencion = new Atencion();
        atencion.setCita(cita);
        atencion.setFecha(LocalDate.now());
        atencion.setMotivo(dto.getMotivo());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setObservaciones(dto.getObservaciones());

        if (dto.getPatologiaId() != null) {
            Patologia p = patologiaRepository.findById(dto.getPatologiaId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Patología no encontrada"));
            atencion.setPatologia(p);
        }

        return atencionRepository.save(atencion);
    }

    public List<AtencionResponseDTO> listarPorUsuario(Long usuarioId) {
        return atencionRepository.findByCitaPacienteId(usuarioId)
                .stream()
                .map(AtencionResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<Atencion> findByPacienteId(Long pacienteId) {
        return atencionRepository.findByCitaPacienteId(pacienteId);
    }

    public List<AtencionDetailDTO> findAtencionesDetailByPacienteId(Long pacienteId) {
        List<Atencion> atenciones = atencionRepository.findByCitaPacienteId(pacienteId);
        return atenciones.stream()
                .map(AtencionDetailDTO::new)
                .collect(Collectors.toList());
    }
}
