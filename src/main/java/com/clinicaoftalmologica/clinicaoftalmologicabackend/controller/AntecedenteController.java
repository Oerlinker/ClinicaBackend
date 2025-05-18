package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AntecedenteResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.AntecedenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antecedentes")
@RequiredArgsConstructor
public class AntecedenteController {

    private final AntecedenteService service;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'ENFERMERA')")
    @GetMapping("/usuario/{usuarioId}")
    public List<AntecedenteResponseDTO> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'ENFERMERA')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AntecedenteResponseDTO crear(@RequestBody AntecedenteRegisterDTO dto) {
        return service.crear(dto);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'ENFERMERA')")
    @PutMapping("/{id}")
    public AntecedenteResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody AntecedenteRegisterDTO dto
    ) {
        return service.actualizar(id, dto);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}