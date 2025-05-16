package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.AtencionRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Atencion;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.AtencionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
public class AtencionController {

    private final AtencionService atencionService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR')")
    public ResponseEntity<Atencion> registrar(@RequestBody AtencionRegisterDTO dto) {
        return ResponseEntity.ok(atencionService.registrar(dto));
    }
}