package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DepartamentoCreateDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DepartamentoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService service;


    @GetMapping
    public List<DepartamentoDTO> listar() {
        return service.listarTodos();
    }


    @GetMapping("/{id}")
    public DepartamentoDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }


    @PostMapping
    public ResponseEntity<DepartamentoDTO> crear(
            @RequestBody DepartamentoCreateDTO dto
    ) {
        DepartamentoDTO creado = service.crear(dto);
        return ResponseEntity
                .status(201)
                .body(creado);
    }


    @PutMapping("/{id}")
    public DepartamentoDTO actualizar(
            @PathVariable Long id,
            @RequestBody DepartamentoCreateDTO dto
    ) {
        return service.actualizar(id, dto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}