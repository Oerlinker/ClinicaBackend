package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TriajeCreateDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TriajeDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.TriajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/triajes")
public class TriajeController {

    @Autowired
    private TriajeService service;
    @PreAuthorize("hasAnyAuthority('ENFERMERA')")
    @PostMapping
    public ResponseEntity<TriajeDTO> crear(@RequestBody TriajeCreateDTO dto) {
        TriajeDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    @PreAuthorize("hasAnyAuthority('ENFERMERA','DOCTOR')")
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<TriajeDTO> obtenerPorCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(service.obtenerPorCita(citaId));
    }
    @PreAuthorize("hasAnyAuthority('ENFERMERA','DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}