package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.PatologiaResponseDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.PatologiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patologias")
public class PatologiaController {

    private final PatologiaService service;

    @Autowired
    public PatologiaController(PatologiaService service) {
        this.service = service;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    @GetMapping
    public List<PatologiaResponseDTO> listar() {
        return service.getAll();
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public PatologiaResponseDTO obtener(@PathVariable Long id) {
        return service.getById(id);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatologiaResponseDTO crear(@RequestBody PatologiaRegisterDTO dto) {
        return service.create(dto);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public PatologiaResponseDTO actualizar(@PathVariable Long id,
                                           @RequestBody PatologiaRegisterDTO dto) {
        return service.update(id, dto);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.delete(id);
    }
}
