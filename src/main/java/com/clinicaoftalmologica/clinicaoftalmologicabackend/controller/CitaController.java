package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.aop.Loggable;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.CitaEstado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Usuario;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.CitaService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PACIENTE')")
    @PostMapping
    public ResponseEntity<?> createCita(
            @RequestBody Map<String, Object> data,
            @AuthenticationPrincipal Usuario authUser) {
        try {
            logger.info("Datos recibidos para crear cita: {}", data);

            Long doctorId = null;
            Long pacienteId = null;

            if (data.get("doctorId") != null) {
                doctorId = Long.valueOf(String.valueOf(data.get("doctorId")));
            } else if (data.get("doctor") != null && data.get("doctor") instanceof Map) {
                Map<String, Object> doctorMap = (Map<String, Object>) data.get("doctor");
                if (doctorMap.get("id") != null) {
                    doctorId = Long.valueOf(String.valueOf(doctorMap.get("id")));
                }
            }

            if ("PACIENTE".equalsIgnoreCase(authUser.getRol().getNombre())) {
                pacienteId = authUser.getId();
            } else {
                if (data.get("pacienteId") != null) {
                    pacienteId = Long.valueOf(String.valueOf(data.get("pacienteId")));
                } else if (data.get("paciente") != null && data.get("paciente") instanceof Map) {
                    Map<String, Object> pacienteMap = (Map<String, Object>) data.get("paciente");
                    if (pacienteMap.get("id") != null) {
                        pacienteId = Long.valueOf(String.valueOf(pacienteMap.get("id")));
                    }
                }
            }

            if (doctorId == null) {
                logger.error("No se encontró doctorId en los datos: {}", data);
                return ResponseEntity.badRequest().body("El ID del doctor es requerido");
            }

            if (pacienteId == null) {
                logger.error("No se encontró pacienteId en los datos: {}", data);
                return ResponseEntity.badRequest().body("El ID del paciente es requerido");
            }

            logger.info("IDs extraídos: doctorId={}, pacienteId={}", doctorId, pacienteId);

            Cita cita = new Cita();

            String fechaStr = String.valueOf(data.get("fecha"));
            String horaStr = String.valueOf(data.get("hora"));

            logger.info("Fecha recibida: {}", fechaStr);
            logger.info("Hora recibida: {}", horaStr);

            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);
                cita.setFecha(fecha);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(horaStr, timeFormatter);

                LocalDateTime fechaHora = LocalDateTime.of(fecha, time);
                logger.info("Fecha y hora combinadas: {}", fechaHora);
                cita.setHora(fechaHora);
            } catch (DateTimeParseException e) {
                logger.error("Error al parsear fecha u hora: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Formato de fecha u hora inválido: " + e.getMessage());
            }

            if (data.get("estado") != null) {
                cita.setEstado(CitaEstado.valueOf(data.get("estado").toString().toUpperCase()));
            } else {
                cita.setEstado(CitaEstado.PENDIENTE);
            }

            if (data.get("tipo") != null) {
                String tipo = String.valueOf(data.get("tipo"));
                cita.setTipo(tipo);

                Long precio;

                switch (tipo.toLowerCase()) {
                    case "rutina":
                        precio = 5000L;
                        break;
                    case "control":
                        precio = 7000L;
                        break;
                    case "pediátrica":
                        precio = 6000L;
                        break;
                    case "pre-quirúrgica":
                        precio = 8000L;
                        break;
                    case "post-quirúrgica":
                        precio = 9000L;
                        break;
                    default:
                        precio = 5000L;
                }
                cita.setPrecio(precio);
            } else {
                cita.setTipo("CONSULTA");
                cita.setPrecio(5000L);
            }

            Cita nuevaCita = citaService.createCita(cita, doctorId, pacienteId);
            return ResponseEntity.ok(nuevaCita);
        } catch (Exception e) {
            logger.error("Error al crear cita: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
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
            @AuthenticationPrincipal Usuario user) {
        try {
            Cita cita = citaService.cancelarCitaPorPaciente(id, user.getId());
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PatchMapping("/{id}/realizar")
    public ResponseEntity<?> realizarCita(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario user) {
        try {
            Cita cita = citaService.marcarRealizadaPorDoctor(id, user.getId());
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PatchMapping("/{id}/cancelar-doctor")
    public ResponseEntity<?> cancelarCitaPorDoctor(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario user) {
        try {
            Cita cita = citaService.cancelarCitaPorDoctor(id, user.getId());
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @GetMapping("/mis-citas-doctor")
    public ResponseEntity<List<Cita>> getMisCitasDoctor(Principal principal) {
        String username = principal.getName();
        Usuario authUser = usuarioService.obtenerPorUsername(username);
        List<Cita> citas = citaService.getCitasByDoctorId(authUser.getId());
        return ResponseEntity.ok(citas);
    }
}