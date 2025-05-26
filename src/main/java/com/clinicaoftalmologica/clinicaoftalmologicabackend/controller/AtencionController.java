package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionDetailDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.AtencionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
public class AtencionController {

    private final AtencionService atencionService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Atencion> registrar(@RequestBody AtencionRegisterDTO dto) {
        Atencion atencion = atencionService.registrar(dto);
        return ResponseEntity.ok(atencion);
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AtencionDetailDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<AtencionDetailDTO> atenciones = atencionService.findAtencionesDetailByPacienteId(pacienteId);
        return ResponseEntity.ok(atenciones);
    }
}
