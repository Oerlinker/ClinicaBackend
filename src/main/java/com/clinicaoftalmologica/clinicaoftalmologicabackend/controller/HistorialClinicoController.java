package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;


import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.HistorialClinicoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.HistorialClinicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
public class HistorialClinicoController {
    private final HistorialClinicoService service;

    @GetMapping("/usuario/{usuarioId}")
    public HistorialClinicoDTO obtenerHistorial(@PathVariable Long usuarioId) {
        return service.getHistorialPorUsuario(usuarioId);
    }
}