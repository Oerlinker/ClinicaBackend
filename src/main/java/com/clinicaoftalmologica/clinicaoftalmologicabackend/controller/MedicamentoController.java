package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.MedicamentoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicamentoDTO>> getAllMedicamentos() {
        return ResponseEntity.ok(medicamentoService.getAllMedicamentos());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<MedicamentoDTO>> searchMedicamentos(@RequestParam String nombre) {
        return ResponseEntity.ok(medicamentoService.searchMedicamentosByNombre(nombre));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicamentoDTO> getMedicamentoById(@PathVariable Long id) {
        return medicamentoService.getMedicamentoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MedicamentoDTO> createMedicamento(@RequestBody MedicamentoDTO medicamentoDTO) {
        return new ResponseEntity<>(medicamentoService.createMedicamento(medicamentoDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MedicamentoDTO> updateMedicamento(
            @PathVariable Long id,
            @RequestBody MedicamentoDTO medicamentoDTO) {
        return medicamentoService.updateMedicamento(id, medicamentoDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteMedicamento(@PathVariable Long id) {
        boolean deleted = medicamentoService.deleteMedicamento(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("eliminado", true));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
