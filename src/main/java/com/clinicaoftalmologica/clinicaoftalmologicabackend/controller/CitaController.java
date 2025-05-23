package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.*;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EmpleadoRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.ServicioRepository;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.CitaService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ServicioRepository servicioRepository;


    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA')")
    @GetMapping
    public ResponseEntity<List<Cita>> getAllCitas() {
        return ResponseEntity.ok(citaService.getAllCitas());
    }


    @PreAuthorize("hasAuthority('PACIENTE')")
    @GetMapping("/mis-citas")
    public ResponseEntity<List<Cita>> getMisCitas(Principal principal) {
        Usuario u = usuarioService.obtenerPorUsername(principal.getName());
        return ResponseEntity.ok(citaService.getCitasByPacienteId(u.getId()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','PACIENTE','SECRETARIA')")
    @PostMapping
    public ResponseEntity<?> createCita(
            @RequestBody Map<String, Object> data,
            Principal principal
    ) {
        try {
            logger.info("Datos recibidos para crear cita: {}", data);

            Usuario authUser = usuarioService.obtenerPorUsername(principal.getName());
            String userRole = authUser.getRol().getNombre();

            Long doctorId = null;
            Long pacienteId = null;

            if (data.get("doctorId") != null) {
                doctorId = Long.valueOf(data.get("doctorId").toString());
            } else if (data.get("doctor") instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) data.get("doctor");
                doctorId = Long.valueOf(m.get("id").toString());
            }

            if ("PACIENTE".equalsIgnoreCase(userRole)) {
                pacienteId = authUser.getId();
            } else if (data.get("pacienteId") != null) {
                pacienteId = Long.valueOf(data.get("pacienteId").toString());
            } else if (data.get("paciente") instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) data.get("paciente");
                pacienteId = Long.valueOf(m.get("id").toString());
            }

            if (doctorId == null) {

                return ResponseEntity.badRequest().body(Map.of("error", "El ID del doctor es requerido"));
            }
            if (pacienteId == null) {

                return ResponseEntity.badRequest().body(Map.of("error", "El ID del paciente es requerido"));
            }
            logger.info("IDs extraídos: doctorId={}, pacienteId={}", doctorId, pacienteId);
            Cita cita = new Cita();
            String fechaStr = data.get("fecha").toString();
            String horaStr = data.get("hora").toString();

            try {
                LocalDate fecha = LocalDate.parse(fechaStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                LocalTime hora = LocalTime.parse(horaStr,
                        DateTimeFormatter.ofPattern("HH:mm:ss"));
                cita.setFecha(fecha);
                cita.setHora(LocalDateTime.of(fecha, hora));
            } catch (DateTimeParseException e) {
                logger.error("Error al parsear fecha/hora: fecha='{}', hora='{}', error='{}'", fechaStr, horaStr, e.getMessage());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Formato de fecha u hora inválido: " + e.getMessage()));
            }

            if ("SECRETARIA".equalsIgnoreCase(userRole)) {
                cita.setEstado(CitaEstado.AGENDADA);
                logger.info("Usuario SECRETARIA detectado. Estableciendo estado a AGENDADA.");
            } else {

                if (data.get("estado") != null) {
                    cita.setEstado(
                            CitaEstado.valueOf(data.get("estado").toString().toUpperCase())
                    );
                } else {
                    cita.setEstado(CitaEstado.PENDIENTE);
                    logger.info("Usuario no SECRETARIA. Estableciendo estado a PENDIENTE.");
                }
            }

            final Long servicioIdFinal;
            Long tempServicioId = null;

            if (data.get("servicioId") != null) {
                tempServicioId = Long.valueOf(data.get("servicioId").toString());
            } else if (data.get("servicio") instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) data.get("servicio");
                tempServicioId = Long.valueOf(m.get("id").toString());
            }
            servicioIdFinal = tempServicioId;


            if (servicioIdFinal == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID del servicio es requerido"));
            }

            Servicio servicio = servicioRepository.findById(servicioIdFinal)
                    .orElseThrow(() -> new Exception("Servicio no encontrado con ID: " + servicioIdFinal));
            cita.setServicio(servicio);
            cita.setPrecio(servicio.getPrecio());

            Cita nueva = citaService.createCita(cita, doctorId, pacienteId);
            return ResponseEntity.ok(nueva);

        } catch (Exception e) {
            logger.error("Error al crear cita: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLEADO')")
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Cita>> getCitasByUsuario(@PathVariable Long userId) {
        logger.info("Solicitando citas para el usuario con ID: {}", userId);
        List<Cita> citas = citaService.getCitasByPacienteId(userId);
        return ResponseEntity.ok(citas);
    }

    @PreAuthorize("hasAuthority('PACIENTE')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCita(
            @PathVariable Long id,
            Principal principal) {
        try {
            String username = principal.getName();
            Usuario usuario = usuarioService.obtenerPorUsername(username);
            Cita c = citaService.cancelarCitaPorPaciente(id, usuario.getId());
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            logger.error("Error al cancelar cita: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PatchMapping("/{id}/realizar")
    public ResponseEntity<?> realizarCita(
            @PathVariable Long id,
            Principal principal) {
        try {
            String username = principal.getName();
            Usuario usuario = usuarioService.obtenerPorUsername(username);
            Empleado doctor = empleadoRepository
                    .findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            Cita c = citaService.marcarRealizadaPorDoctor(id, doctor.getId());
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            logger.error("Error al marcar cita como realizada: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PatchMapping("/{id}/cancelar-doctor")
    public ResponseEntity<?> cancelarCitaPorDoctor(
            @PathVariable Long id,
            Principal principal) {
        try {
            String username = principal.getName();
            Usuario usuario = usuarioService.obtenerPorUsername(username);
            Empleado doctor = empleadoRepository
                    .findByUsuarioId(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            Cita c = citaService.cancelarCitaPorDoctor(id, doctor.getId());
            return ResponseEntity.ok(c);
        } catch (Exception e) {
            logger.error("Error al cancelar cita por doctor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/mis-citas-doctor")
    public ResponseEntity<List<Cita>> getMisCitasDoctor(Principal principal) {
        String username = principal.getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username);
        Empleado doctor = empleadoRepository
                .findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Empleado no encontrado para usuario " + username
                ));

        List<Cita> citas = citaService.getCitasByDoctorId(doctor.getId());
        return ResponseEntity.ok(citas);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable Long id) {
        try {
            citaService.deleteCita(id);
            return ResponseEntity.ok(Map.of("message", "Cita con ID " + id + " eliminada correctamente."));
        } catch (Exception e) {
            logger.error("Error al eliminar cita con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA','PACIENTE')")
    @GetMapping("/doctor/{doctorId}/fecha/{fecha}")
    public ResponseEntity<List<Cita>> getCitasByDoctorAndFecha(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.getCitasByDoctorAndFecha(doctorId, fecha);
        return ResponseEntity.ok(citas);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENFERMERA')")
    @GetMapping("/pendientes-triaje")
    public ResponseEntity<List<Cita>> getCitasPendientesTriaje() {
        return ResponseEntity.ok(citaService.getCitasPendientesTriaje());
    }
}