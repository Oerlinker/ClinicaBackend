package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.AtencionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
public class AtencionController {

    private final AtencionService atencionService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<AtencionResponseDTO> registrar(@RequestBody AtencionRegisterDTO dto) {
        Atencion atencion = atencionService.registrar(dto);
        // Convertir la entidad a DTO antes de devolverla
        AtencionResponseDTO responseDTO = new AtencionResponseDTO(atencion);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyAuthority('DOCTOR','ADMIN','PACIENTE')")
    public ResponseEntity<List<AtencionResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<AtencionResponseDTO> list = atencionService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AtencionResponseDTO>> listarPorPacienteYFiltros(
            @PathVariable Long pacienteId,
            @RequestBody(required = false) Map<String, Long> filtros
    ) {
        Long doctorId = filtros != null ? filtros.get("doctorId") : null;
        List<AtencionResponseDTO> list = atencionService.filtrar(pacienteId, doctorId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR','SECRETARIA')")
    public ResponseEntity<AtencionResponseDTO> detalle(@PathVariable Long id) {
        AtencionResponseDTO dto = atencionService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }
}
