package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.MedicamentoTratamientoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.TratamientoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.TratamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoService tratamientoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    public ResponseEntity<List<TratamientoDTO>> getAllTratamientos() {
        return ResponseEntity.ok(tratamientoService.getAllTratamientos());
    }

    @GetMapping("/atencion/{atencionId}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<List<TratamientoDTO>> getTratamientosByAtencion(@PathVariable Long atencionId) {
        return ResponseEntity.ok(tratamientoService.getTratamientosByAtencion(atencionId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<TratamientoDTO> getTratamientoById(@PathVariable Long id) {
        return tratamientoService.getTratamientoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<?> createTratamiento(@RequestBody TratamientoDTO tratamientoDTO) {
        try {
            return new ResponseEntity<>(tratamientoService.createTratamiento(tratamientoDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<?> updateTratamiento(
            @PathVariable Long id,
            @RequestBody TratamientoDTO tratamientoDTO) {
        try {
            return tratamientoService.updateTratamiento(id, tratamientoDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<Map<String, Boolean>> deleteTratamiento(@PathVariable Long id) {
        boolean deleted = tratamientoService.deleteTratamiento(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("eliminado", true));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints para gestionar los medicamentos de un tratamiento
    @GetMapping("/{tratamientoId}/medicamentos")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN', 'PACIENTE')")
    public ResponseEntity<?> getMedicamentosByTratamiento(@PathVariable Long tratamientoId) {
        try {
            List<MedicamentoTratamientoDTO> medicamentos = tratamientoService.getMedicamentosByTratamiento(tratamientoId);
            return ResponseEntity.ok(medicamentos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{tratamientoId}/medicamentos")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<?> asociarMedicamentoATratamiento(
            @PathVariable Long tratamientoId,
            @RequestBody MedicamentoTratamientoDTO medicamentoTratamientoDTO) {
        try {
            MedicamentoTratamientoDTO result =
                tratamientoService.asociarMedicamentoATratamiento(tratamientoId, medicamentoTratamientoDTO);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{tratamientoId}/medicamentos/{medicamentoTratamientoId}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<?> eliminarMedicamentoDeTratamiento(
            @PathVariable Long tratamientoId,
            @PathVariable Long medicamentoTratamientoId) {
        try {
            boolean deleted = tratamientoService.eliminarMedicamentoDeTratamiento(tratamientoId, medicamentoTratamientoId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("eliminado", true));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
