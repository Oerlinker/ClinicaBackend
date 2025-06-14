package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TratamientoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Patologia;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Tratamiento;
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
    private final TratamientoService tratamientoService;

    @Transactional
    public Atencion registrar(AtencionRegisterDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        Atencion atencion = new Atencion();
        atencion.setCita(cita);
        atencion.setFecha(LocalDate.now());
        atencion.setMotivo(dto.getMotivo());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setObservaciones(dto.getObservaciones());

        if (dto.getPatologiaId() != null) {
            Patologia p = patologiaRepository.findById(dto.getPatologiaId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Patología no encontrada"));
            atencion.setPatologia(p);
        }

        // Guardar la atención primero para obtener su ID
        Atencion atencionGuardada = atencionRepository.save(atencion);

        // Procesar los tratamientos si existen
        if (dto.getTratamientos() != null && !dto.getTratamientos().isEmpty()) {
            for (TratamientoDTO tratamientoDTO : dto.getTratamientos()) {
                // Establecer la referencia a la atención recién guardada
                tratamientoDTO.setAtencionId(atencionGuardada.getId());
                try {
                    tratamientoService.createTratamiento(tratamientoDTO);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Error al crear tratamiento: " + e.getMessage());
                }
            }

            // Recargar la atención para incluir los tratamientos creados
            atencionGuardada = atencionRepository.findById(atencionGuardada.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al recargar la atención"));
        }

        return atencionGuardada;
    }

    public List<AtencionResponseDTO> listarPorUsuario(Long usuarioId) {
        return atencionRepository.findByCitaPacienteId(usuarioId)
                .stream()
                .map(AtencionResponseDTO::new)
                .collect(Collectors.toList());
    }
}
