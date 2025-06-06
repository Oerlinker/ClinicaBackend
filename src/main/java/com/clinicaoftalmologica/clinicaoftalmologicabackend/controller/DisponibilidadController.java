package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.DisponibilidadInitService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.DisponibilidadService;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService service;

    @Autowired
    private DisponibilidadInitService disponibilidadInitService;

    @Autowired
    private EmpleadoService empleadoService;


    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA')")
    @PostMapping
    public ResponseEntity<Disponibilidad> crear(@RequestBody Disponibilidad d) {
        return ResponseEntity.ok(service.crear(d));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA')")
    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(
            @PathVariable Long id,
            @RequestBody Disponibilidad d) {
        d.setId(id);
        return ResponseEntity.ok(service.actualizar(d));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA','PACIENTE')")
    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<Disponibilidad>> obtenerPorEmpleado(
            @PathVariable Long empleadoId) {
        List<Disponibilidad> lista = service.obtenerPorEmpleado(empleadoId);
        return ResponseEntity.ok(lista);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA','PACIENTE')")
    @GetMapping("/empleado/{empleadoId}/fecha/{fecha}")
    public ResponseEntity<Disponibilidad> obtenerPorEmpleadoYFecha(
            @PathVariable Long empleadoId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return service.obtenerPorEmpleadoYFecha(empleadoId, fecha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA','PACIENTE')")
    @GetMapping("/empleado/{empleadoId}/slots")
    public ResponseEntity<DisponibilidadDTO> obtenerSlots(
            @PathVariable Long empleadoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        DisponibilidadDTO dto =
                service.getDisponibilidadWithSlots(empleadoId, fecha);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/generar/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','SECRETARIA')")
    public ResponseEntity<?> generarDisponibilidades(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            disponibilidadInitService.crearDisponibilidadesParaDoctor(
                    doctorId,
                    fechaInicio,
                    fechaFin
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generar-todos")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> generarDisponibilidadesTodos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Empleado> doctores = empleadoService.getDoctores();
            for (Empleado doctor : doctores) {
                disponibilidadInitService.crearDisponibilidadesParaDoctor(
                        doctor.getId(),
                        fechaInicio,
                        fechaFin
                );
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
