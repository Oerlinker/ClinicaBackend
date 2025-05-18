package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.ServicioResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService service;

    @GetMapping
    public List<ServicioResponseDTO> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ServicioResponseDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicioResponseDTO crear(@RequestBody ServicioRegisterDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}")
    public ServicioResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody ServicioRegisterDTO dto
    ) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}