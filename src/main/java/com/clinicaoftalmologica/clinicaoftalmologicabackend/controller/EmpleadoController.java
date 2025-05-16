package com.clinicaoftalmologica.clinicaoftalmologicabackend.controller;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.EmpleadoRegisterDTO;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Empleado;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAllEmpleados() {
        return ResponseEntity.ok(
            empleadoService.getAllEmpleados().stream()
                .map(EmpleadoDTO::new)
                .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<EmpleadoDTO> createEmpleado(@RequestBody EmpleadoRegisterDTO dto) {
        try {
            Empleado nuevoEmpleado = empleadoService.createEmpleado(dto);
            return ResponseEntity.ok(new EmpleadoDTO(nuevoEmpleado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(@PathVariable Long id, @RequestBody EmpleadoRegisterDTO dto) {
        try {
            Empleado updatedEmpleado = empleadoService.updateEmpleado(id, dto);
            return ResponseEntity.ok(new EmpleadoDTO(updatedEmpleado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id) {
        try {
            empleadoService.deleteEmpleado(id);
            return ResponseEntity.ok("Empleado eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/doctores")
    public ResponseEntity<List<EmpleadoDTO>> getDoctores() {
        try {
            List<EmpleadoDTO> doctoresDTO = empleadoService.getDoctores().stream()
                    .map(EmpleadoDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(doctoresDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/sin-departamento")
    public ResponseEntity<List<EmpleadoDTO>> getSinDepartamento() {
        return ResponseEntity.ok(empleadoService.listarSinDepartamento());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/departamento/{deptId}")
    public ResponseEntity<List<EmpleadoDTO>> getPorDepartamento(@PathVariable Long deptId) {
        return ResponseEntity.ok(empleadoService.listarPorDepartamento(deptId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{empId}/departamento/{deptId}")
    public ResponseEntity<?> asignarDepartamento(
            @PathVariable Long empId,
            @PathVariable Long deptId) {
        try {
            empleadoService.asignarDepartamento(empId, deptId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{empId}/sin-departamento")
    public ResponseEntity<?> quitarDepartamento(@PathVariable Long empId) {
        try {
            empleadoService.quitarDepartamento(empId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/todos")
    public ResponseEntity<List<EmpleadoDTO>> getTodos() {
        return ResponseEntity.ok(
                empleadoService.getAllEmpleados().stream()
                        .map(EmpleadoDTO::new)
                        .toList()
        );
    }
}
